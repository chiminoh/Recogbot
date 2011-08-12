#include "devices/kinect.h"
#include <iostream>
#include "opencv2/opencv.hpp"
#include "devices/Whell.h"
using namespace Recogbot;
using namespace std;

void main(void) {
	Kinect kinect;
	kinect.init();
	
	IplImage *frame = cvCreateImage(cvSize(640,480),8,3);
	IplImage *depth8 = cvCreateImage(cvSize(320,240),8,1);
	
	while(1) {
		kinect.videoImage(frame);
		kinect.depth8BitImage(depth8);
		cvShowImage("depth",depth8);
		cvShowImage("color",frame);
		cvWaitKey(10);
	}
}