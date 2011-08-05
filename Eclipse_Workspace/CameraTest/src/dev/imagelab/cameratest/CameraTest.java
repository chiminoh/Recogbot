package dev.imagelab.cameratest;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Toast;

public class CameraTest extends Activity implements SurfaceHolder.Callback{
    /** Called when the activity is first created. */

    Camera mCamera;
    boolean mPreviewRunning = false;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Bitmap viewImage  = null;
    private DrawView drawBitmap = null;
    private BitmapFactory.Options options;
    private int imageCnt = 0;
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        //options.inPreferredConfig = Bitmap.Config.RGB_565;
        
        //viewImage = new Bitmap[5];
        
        setContentView(R.layout.main);
        mSurfaceView = (SurfaceView)findViewById(R.id.surface);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        drawBitmap = new DrawView(this);
        
	    addContentView(drawBitmap, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    
	    
        Button snapshoot = (Button) findViewById(R.id.Snapshoot);
        snapshoot.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	drawBitmap.setVisibility(View.VISIBLE);
            	mCamera.takePicture(null, null, mPictureCallback);
            	//mCamera.takePicture(null, null, mPictureCallback);
            	//mCamera.takePicture(null, null, mPictureCallback);
            	//mCamera.takePicture(null, null, mPictureCallback);
            	//mCamera.takePicture(null, null, mPictureCallback);
            	//faceGallery.setVisibility(View.GONE);
            	//drawBitmap.capture(viewImage);
            }
        });
        
       Button addface = (Button) findViewById(R.id.AddFace);
       addface.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	drawBitmap.setVisibility(View.VISIBLE);
            	drawBitmap.saveFaceImage();
            }
        });
       
       Button imageTest = (Button) findViewById(R.id.imgaeTest);
       imageTest.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	drawBitmap.setVisibility(View.VISIBLE);
            	mCamera.stopPreview();
            	drawBitmap.imageTest();
            }
        });
       
       Button returnPreview = (Button) findViewById(R.id.returnPreview);
       returnPreview.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	drawBitmap.setVisibility(View.INVISIBLE);
            	drawBitmap.setFocusable(false);
            	mCamera.stopPreview();
            	mCamera.startPreview();
            }
        });       
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
    	
    	//mCamera.takePicture(null, null, mPictureCallback);
    	
        return true;
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        }

        if (keyCode == KeyEvent.KEYCODE_CAMERA) {
        	drawBitmap.setVisibility(View.VISIBLE);
        	mCamera.takePicture(null, null, mPictureCallback);
        	return true;
        }
        return false;
    }
    
    PictureCallback mPictureCallback =new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {

	        //viewImage[imageCnt] = BitmapFactory.decodeByteArray(data, 0, data.length, options);
	        viewImage = BitmapFactory.decodeByteArray(data, 0, data.length, options);
	        drawBitmap.capture(viewImage);
	        //if(imageCnt++ == 5)imageCnt = 0;
		}
	};

  	void releaseCamera(){
			if(mCamera != null)
				mCamera.release();
		}	
    protected void onResume()
    {
      //  Log.e(TAG, "onResume");
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    protected void onStop()
    {
        //Log.e(TAG, "onStop");
        super.onStop();
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
        //Log.e(TAG, "surfaceCreated");
        mCamera = Camera.open();
        //mCamera.startPreview();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
       // Log.e(TAG, "surfaceChanged");

        if (mPreviewRunning) {
            mCamera.stopPreview();
        }
        
        Camera.Parameters params = mCamera.getParameters();   	    	 
      	//mSize = params.getPictureSize();
      	//params.setPictureFormat(PixelFormat.JPEG);    	     	
      	params.setPreviewSize(w, h);
      	mCamera.setParameters(params);

        try{
        mCamera.setPreviewDisplay(holder);
        }catch(Exception e) 
		{
			new AlertDialog.Builder(CameraTest.this).setTitle("Error")
			.setMessage(e.getMessage())
			.setPositiveButton("OK", null).show();
		}
        mCamera.startPreview();
        mPreviewRunning = true;
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //Log.e(TAG, "surfaceDestroyed");
        mCamera.stopPreview();
        mPreviewRunning = false;
        mCamera.release();
    }
}