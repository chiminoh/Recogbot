package dev.imagelab.gallaxyfacedetection;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class GallaxyFaceDetection extends Activity {
    /** Called when the activity is first created. */
	//FaceDetectionView faceView;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  // 화면 밝기 유지
	        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀 없게
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 가로 화면 유지
	        
	        //faceView = new FaceDetectionView(this);
	        

	        //setContentView(new FaceDetectionView(this));
	        setContentView(R.layout.main);
	        addContentView(new FaceDetectionView(this), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    }
}