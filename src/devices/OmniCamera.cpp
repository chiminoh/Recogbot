#include "devices/omnicamera.h"
#include <cstdio>
//#include <cstdlib>
#include <cstring>
#include <windows.h>
#include <process.h>
// callback called when the camera is plugged/unplugged
#define CHECK_ERR(fnc, amsg)										\
	do {															\
		tPvErr err = fnc;											\
		if (err != ePvErrSuccess) {									\
			char msg[256];											\
			snprintf(msg, 256, "%s: %s", amsg, errorStrings[err]);	\
			throw ProsilicaException(err, msg);						\
	}																\
} while (false)

namespace Recogbot {
	void CameraEventCB(void* Context,
								 tPvInterface Interface,
								 tPvLinkEvent Event,
								 unsigned long UniqueId)
	{
		switch(Event)
		{
			case ePvLinkAdd:
			{
				printf("camera %lu plugged\n",UniqueId);
				break;
			}
			case ePvLinkRemove:
			{
				printf("camera %lu unplugged\n",UniqueId);
				break;
			}
			default:
				break;
		}
	}

	OmniCamera::OmniCamera() {
		memset(&_camera,0,sizeof(tCamera));
		_connected=false;
		_width = 0;
		_height = 0;
	}

	OmniCamera::~OmniCamera() {
		stop();
	}

	bool OmniCamera::connect(void) {
		if(isConnected())
			return true;

		tPvCameraInfoEx info[1];
		 unsigned long frameSize = 0;

		//PvLinkCallbackRegister(CameraEventCB,ePvLinkAdd,NULL);
        //PvLinkCallbackRegister(CameraEventCB,ePvLinkRemove,NULL);        

		if(!PvInitialize()) {

			while(!PvCameraCount()) 
				Sleep(250);


			unsigned long numCameras = PvCameraListEx(info, 1, NULL, sizeof(tPvCameraInfoEx));;
								
			if ((numCameras == 1) && (info[0].PermittedAccess & ePvAccessMaster)) {
				
				_camera.UID = info[0].UniqueId;

				if(!PvCameraOpen(_camera.UID, ePvAccessMaster, &(_camera.handle))) {

					if(!PvAttrUint32Get(_camera.handle,"TotalBytesPerFrame",&frameSize)) {

						_camera.frame.ImageBuffer = new char[frameSize];
						
						unsigned long format=0;
						PvAttrEnumSet(_camera.handle,"PixelFormat","Bayer8");
						char text[100];
						PvAttrEnumGet(_camera.handle,"PixelFormat",text,sizeof(text),&format);
						printf("format %d %s\n",format,text);
						if(_camera.frame.ImageBuffer) {
							_camera.frame.ImageBufferSize = frameSize;
									
							PvAttrUint32Get(_camera.handle,"Width",&_width);
							PvAttrUint32Get(_camera.handle,"Height",&_height);
							PvCaptureStart(_camera.handle);
							PvAttrEnumSet(_camera.handle, "AcquisitionMode", "Continuous");
							PvCommandRun(_camera.handle, "AcquisitionStart");

							_connected = true;
							return true;
						}
					}
				}
			}				
		}
		_connected = false;
		return false;
	}

	void OmniCamera::stop(void) {
		if(isConnected()) {
			PvCommandRun(_camera.handle,"AcquisitionStop");
            PvCaptureEnd(_camera.handle);
			PvCameraClose(_camera.handle);
			if(_camera.frame.ImageBuffer)
				delete [] (char*)_camera.frame.ImageBuffer;
			_camera.handle = NULL;
			_connected = false;
		}
	}

	void OmniCamera::grab(IplImage *img) {
		//Sleep(400);
		PvCaptureQueueFrame(_camera.handle,&(_camera.frame),0);
		printf("waiting for the frame ...\n");
        PvCaptureWaitForFrameDone(_camera.handle,&(_camera.frame),PVINFINITE);
		printf("frame's done ...\n, width %d height %d depth %d size %d\n",_camera.frame.Width,_camera.frame.Height,_camera.frame.BitDepth, _camera.frame.ImageBufferSize);
		
		PvUtilityColorInterpolate(&_camera.frame,&img->imageData[2],&img->imageData[1],&img->imageData[0],2,0);
		
	}

	bool OmniCamera::isConnected(void) {
		return _connected;
	}

}