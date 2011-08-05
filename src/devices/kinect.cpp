#include "devices/kinect.h"
#include <cstdio>
namespace Recogbot {

	Kinect::Kinect() 
	: _initialized(false),
	  _calibrated(false),
	  _pVideoFrame(NULL),
	  _pDepthFrame(NULL) {
		_nextVideoFrameEvent = CreateEvent( NULL, TRUE, FALSE, NULL );
		_nextDepthFrameEvent = CreateEvent( NULL, TRUE, FALSE, NULL );
		_nextSkeletonEvent = CreateEvent( NULL, TRUE, FALSE, NULL );
		_nuiProcessStop=CreateEvent(NULL,FALSE,FALSE,NULL);
		InitializeCriticalSection(&_csVideo);
		InitializeCriticalSection(&_csDepth);
		InitializeCriticalSection(&_csSkeleton);
		_videoImg = cvCreateImage(cvSize(640,480),8,3);
		_depthImg = cvCreateImage(cvSize(320,240),IPL_DEPTH_16U,1);		
	}

	Kinect::~Kinect() {
		uninit();
		DeleteCriticalSection(&_csVideo);
		DeleteCriticalSection(&_csDepth);
		DeleteCriticalSection(&_csSkeleton);
		CloseHandle(_nextVideoFrameEvent);
		CloseHandle(_nextDepthFrameEvent);
		CloseHandle(_nextSkeletonEvent);
		CloseHandle(_nuiProcessStop);
	}

	bool Kinect::init() {
		HRESULT hr;
		hr = NuiInitialize( NUI_INITIALIZE_FLAG_USES_DEPTH_AND_PLAYER_INDEX | NUI_INITIALIZE_FLAG_USES_SKELETON | NUI_INITIALIZE_FLAG_USES_COLOR);
		
		if(hr<0)
			return false;

		hr = NuiSkeletonTrackingEnable( _nextSkeletonEvent, 0 );

		if(hr<0)
			return false;

		hr = NuiImageStreamOpen(
			NUI_IMAGE_TYPE_COLOR,
			NUI_IMAGE_RESOLUTION_640x480,
			0,
			2,
			_nextVideoFrameEvent,
			&_videoHandle );

		if(hr<0)
			return false;

		hr = NuiImageStreamOpen(
			NUI_IMAGE_TYPE_DEPTH_AND_PLAYER_INDEX,
			NUI_IMAGE_RESOLUTION_320x240,
			0,
			2,
			_nextDepthFrameEvent,
			&_depthHandle );

		if(hr<0)
			return false;

		_events[0] = _nuiProcessStop;
		_events[1] = _nextDepthFrameEvent;
		_events[2] = _nextVideoFrameEvent;
		_events[3] = _nextSkeletonEvent;

		CreateThread(NULL,0,nuiProcessThread,this,0,NULL);

		_initialized = true;
		return true;
	}

	void Kinect::uninit() {
		NuiShutdown();
		_initialized=false;
	}

	DWORD WINAPI Kinect::nuiProcessThread(LPVOID pParam) {
		Kinect *pthis = (Kinect *)pParam;
		HRESULT hr;
		KINECT_LOCKED_RECT LockedRect;
		
		while(1) {
			int nEventIdx=WaitForMultipleObjects(sizeof(pthis->_events)/sizeof(pthis->_events[0]),pthis->_events,FALSE,100);
			
			if(nEventIdx==0)
				break;

			switch(nEventIdx){			
				case 1:
					hr = NuiImageStreamGetNextFrame(
						pthis->_depthHandle,
						0,
						&pthis->_pDepthFrame);

					pthis->_pDepthFrame->pFrameTexture->LockRect(0, &LockedRect, NULL, 0);

					EnterCriticalSection(&pthis->_csVideo);
					
					if(LockedRect.Pitch !=0) {
						BYTE* pBuffer = (BYTE*) LockedRect.pBits;
						memcpy(pthis->_depthImg->imageData,pBuffer, (pthis->_depthImg->widthStep)*(pthis->_depthImg->height));
					}
					
					LeaveCriticalSection(&pthis->_csVideo);
					NuiImageStreamReleaseFrame(pthis->_depthHandle,pthis->_pDepthFrame);
					
					break;

				case 2:
					hr = NuiImageStreamGetNextFrame(
						pthis->_videoHandle,
						0,
						&pthis->_pVideoFrame);

					pthis->_pVideoFrame->pFrameTexture->LockRect(0, &LockedRect, NULL, 0);

					EnterCriticalSection(&pthis->_csDepth);

					if( LockedRect.Pitch != 0 ) {
						BYTE* pBuffer = (BYTE*) LockedRect.pBits;

						for( int y = 0 ; y < 480 ; y++ ) {
							for( int x = 0 ; x < 640 ; x++)	 {
								pthis->_videoImg->imageData[y*pthis->_videoImg->widthStep+x*pthis->_videoImg->nChannels+0] = *pBuffer++;
								pthis->_videoImg->imageData[y*pthis->_videoImg->widthStep+x*pthis->_videoImg->nChannels+1] = *pBuffer++;
								pthis->_videoImg->imageData[y*pthis->_videoImg->widthStep+x*pthis->_videoImg->nChannels+2] = *pBuffer++;
								pBuffer++;
							}
						}
					}
					LeaveCriticalSection(&pthis->_csDepth);
					NuiImageStreamReleaseFrame(pthis->_videoHandle,pthis->_pVideoFrame);
					break;

				case 3:
					hr = NuiSkeletonGetNextFrame( 0, &pthis->_skeletonFrame );
					NuiTransformSmooth(&pthis->_skeletonFrame,NULL);
					EnterCriticalSection(&pthis->_csSkeleton);
					//½ºÄÌ·¹Åæ µ¥ÀÌÅÍ º¯È¯ 
					LeaveCriticalSection(&pthis->_csSkeleton);
					break;
			}
		}
		return 0;
	}

	void Kinect::videoImage(IplImage *img) {
		if(_videoImg->widthStep == img->widthStep && _videoImg->height == img->height 
			&& _videoImg->nChannels == img->nChannels && _videoImg->depth == img->depth) {
			EnterCriticalSection(&_csVideo);
			cvCopy(_videoImg,img);
			LeaveCriticalSection(&_csVideo);
		}
	}

	void Kinect::depthImage(IplImage *img) {
		if(_depthImg->widthStep == img->widthStep && _depthImg->height == img->height 
			&& _depthImg->nChannels == img->nChannels && _depthImg->depth == img->depth) {
			EnterCriticalSection(&_csDepth);
			cvCopy(_depthImg,img);
			LeaveCriticalSection(&_csDepth);
		}
	}

	void Kinect::depth8BitImage(IplImage *img) {
		if(_depthImg->width == img->width && _depthImg->height == img->height && img->depth == 8) {
			EnterCriticalSection(&_csDepth);
			cvNormalize(_depthImg,img,0.0,255.0,CV_MINMAX,NULL);
			LeaveCriticalSection(&_csDepth);
		}
	}

	int Kinect::numOfPlayers() {
		return 0;
	}

}