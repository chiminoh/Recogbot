package dev.imagelab.cameratest;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.view.View;
import android.widget.Toast;

public class DrawView extends View { 

	private boolean takePicture;
	private Paint paint = null;
	private Paint tPaint = null;
	private Bitmap bitmap = null;
	private FaceDetector.Face[] faces = null; 
	private FaceDetector detector = null;
	private int numFaces = 0;
	private final int MaxPerson = 4;
	protected Context context = null;
	private OpenCV openCV;
	private FileControl filecontrol = null;
	private Rect boundRect = null;
	private Rect viewRect = null;
	private Rect[] faceRect = null;
	//private static final String TAG = "CameraTest";
	private static final String IMAGE_FOLDER = "IMAGE/";
	//private static final String TEST_FOLDER = "TEST/";
	private static final String DATA_FOLDER = "DATA/";
	private static final String[] searchExtention ={ "JPG", "PNG"}; 
	private Bitmap faceImage[] = null;
	private Bitmap estimateFace[][] = null;
	private boolean saveState = false;
	private boolean testState = false;
	private File imgFile[];
	private int chkCount = 0;
	private static final int imgWidth = 92;
	private static final int imgHeight = 112;
	
	public DrawView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		takePicture = false;
		
		openCV = new OpenCV();
		filecontrol = new FileControl();
		
		paint = new Paint();                                       // UI and Image Boundary Paint
	    paint.setColor(Color.argb(255, 0, 0, 255)); 
	    paint.setStyle(Style.STROKE); 
	    paint.setAntiAlias(true);
	    paint.setStrokeWidth(2); 
	    
	    tPaint = new Paint();                                     // Text paint
	    tPaint.setAntiAlias(true);
	    tPaint.setColor(0x99ffff00); 
	    tPaint.setTextSize(20);
	    tPaint.setStyle(Style.STROKE);
	    
	    boundRect = new Rect(10, 60, 470, 310);
	    viewRect = new Rect(20, 70, 260, 250);
	    faceRect = new Rect[5];
	    faceRect[0] = new Rect(280, 70, 330, 120);
	    faceRect[1] = new Rect(410, 70, 460, 120);
	    faceRect[2] = new Rect(280, 200, 330, 250);
	    faceRect[3] = new Rect(410, 200, 460, 250);
	    faceRect[4] = new Rect(345, 135, 395, 185);
	    faces = new FaceDetector.Face[MaxPerson];
	    this.context = context;
	
	}
	
	public DrawView(Context context, Bitmap initBitmap) {
		super(context);
		// TODO Auto-generated constructor stub
		takePicture = false;
		
		openCV = new OpenCV();
		filecontrol = new FileControl();
		
		this.bitmap = initBitmap;
		
		paint = new Paint();                                       // UI and Image Boundary Paint
	    paint.setColor(Color.argb(255, 0, 0, 255)); 
	    paint.setStyle(Style.STROKE); 
	    paint.setAntiAlias(true);
	    paint.setStrokeWidth(2); 
	    
	    tPaint = new Paint();                                     // Text paint
	    tPaint.setAntiAlias(true);
	    tPaint.setColor(0x99ffff00); 
	    tPaint.setTextSize(20);
	    tPaint.setStyle(Style.STROKE);
	    
	    boundRect = new Rect(10, 60, 470, 310);
	    viewRect = new Rect(20, 70, 260, 250);
	    faceRect = new Rect[5];
	    faceRect[0] = new Rect(280, 70, 330, 120);
	    faceRect[1] = new Rect(410, 70, 460, 120);
	    faceRect[2] = new Rect(280, 200, 330, 250);
	    faceRect[3] = new Rect(410, 200, 460, 200);
	    faceRect[4] = new Rect(345, 135, 395, 185);
	    faces = new FaceDetector.Face[MaxPerson];
	    this.context = context;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		 super.onDraw(canvas);
		 
       if(takePicture == true){
    	   
    	   paint.setColor(Color.argb(255, 0, 0, 255)); 
    	   
    	   takePicture = false;
    	   
    	   if(testState != true){
	    	   detector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), faces.length);
	    	   numFaces = detector.findFaces(bitmap, faces);     // 얼굴 인식 실행
    	   }else{
    		   numFaces = 1;
    	   }
    	   
    	   canvas.drawRect(boundRect, paint);
    	   canvas.drawLine(10, 260, 470, 260, paint);
    	   canvas.drawLine(270, 60, 270, 310, paint);
    	   
    	   tPaint.setTextSize(20);
 		   canvas.drawText("Image Capture...", 60, 290, tPaint); // 텍스트 표시
 		   canvas.drawText("Detected face", 300 , 290, tPaint); // 텍스트 표시
 		  
 		   canvas.drawBitmap(bitmap, null, viewRect, paint);
 		   
	   		if (numFaces > 0) {
	   			faceImage =  new Bitmap[numFaces];
	   			estimateFace = new Bitmap[numFaces][4];
	   			
	   			if(testState != true) saveState = true;
	   			if(testState == true) saveState = false;;
	   		    for (int i = 0; i < numFaces; i++) {
	   		    	if(testState != true){
		   		        Face face = faces[i];
		   		        PointF midPoint = new PointF(0, 0);
		   		        face.getMidPoint(midPoint); 
		   		        float eyesDistance = face.eyesDistance();
		   		        /////////// 얼굴 영역만 //////////////////////
		   		        //int wid = (int)(eyesDistance)>>2;
		   		        //int left =  (int)(midPoint.x - (wid + wid + wid) );
		   		        //int top = (int)(midPoint.y - ( (int)(eyesDistance)>>1) );
		   		        //int wLength = wid *6;
		   		        //int hLength = wLength+wid;
		   		        //////////// 얼굴 전체 ///////////////////////
		   		        int eDist = (int)(eyesDistance);
		   		        int hWidth = eDist>>2;
		   		        int left = (int)( midPoint.x - (eDist + hWidth) );
		   		        if(left <0) left = 0;
		   		        int top = (int)( midPoint.y - (eDist + eDist) );
		   		        if(top <0) top = 0;
		   		        int wLength = eDist + eDist + eDist;
		   		        if(wLength > bitmap.getWidth()) wLength = bitmap.getWidth();
		   		        int hLength = eDist<<2;
		   		        if(hLength > bitmap.getHeight()) hLength = bitmap.getHeight();
		   		        
		   		    	faceImage[i] = Bitmap.createBitmap(bitmap, left,  top, wLength, hLength);
		   		        faceImage[i] = Bitmap.createScaledBitmap(faceImage[i], imgWidth, imgHeight, true);
	   		    	}else{
	   		    		faceImage[i] = this.bitmap;
	   		    	}
	   		        //	Path circle;
	   		        
	   		        //circle = new Path();
	   		        //circle.addCircle(midPoint.x , (int)(midPoint.y+eyesDistance)>>1 , (eyesDistance*1.3F), Direction.CW);
	   		        //canvas.drawPath(circle, paint);                // 빨간 원으로 얼굴 영역 표시
	   		    	switch(i)
	   		        {
	   		        	case 0: 
	   		        		imgFile = filecontrol.searchFile(IMAGE_FOLDER, searchExtention);
		   		     		
	   		        		int[] pixels = new int [imgWidth * imgHeight];
		   		     		faceImage[i].getPixels(pixels, 0, imgWidth, 0, 0, imgWidth, imgHeight);
		   		     		
		   					if(imgFile != null && imgFile.length > 3)
		   					{
     	   					   File[] xmlPath = filecontrol.searchFile(DATA_FOLDER, "XML");
     	   					   if(xmlPath.length == 0) {
	     	   						Bitmap trainFace[] = filecontrol.getImageFile(IMAGE_FOLDER, searchExtention);
		     	   				    int[][] tPixels = new int [trainFace.length][trainFace[0].getWidth() * trainFace[0].getHeight()];
		     	   				   			
		     	   				    for(int k = 0; k < trainFace.length; k++){
		     	   				   		trainFace[k].getPixels(tPixels[k], 0, imgWidth, 0, 0, imgWidth, imgHeight);
		     	   				   	}
		     	   			     	if(openCV.learnFace(tPixels, imgWidth, imgHeight, trainFace.length)) Toast.makeText(context, "Learning about All Face Images" + '[' +trainFace.length + ']' + "...", Toast.LENGTH_SHORT).show();
		     	   			     	else Toast.makeText(context, "Failed to Save!", Toast.LENGTH_SHORT).show();
     	   					   }
				   		     	  int[] result = openCV.faceRecognition(pixels, imgWidth, imgHeight);
				   		     	  estimateFace[i][0] = filecontrol.getImageFile(IMAGE_FOLDER, ("face" + result[0] + ".png") );
				   		     	  estimateFace[i][1] = filecontrol.getImageFile(IMAGE_FOLDER, ("face" + result[1] + ".png") );
				   		     	  estimateFace[i][2] = filecontrol.getImageFile(IMAGE_FOLDER, ("face" + result[2] + ".png") );
				   		     	  estimateFace[i][3] = filecontrol.getImageFile(IMAGE_FOLDER, ("face" + result[3] + ".png") );
				   		     
				   		     	  //canvas.drawBitmap(estimateFace[i][0], 280,70, paint);
			   		        	  //canvas.drawBitmap(estimateFace[i][1], 410,70, paint);
			   		        	  //canvas.drawBitmap(estimateFace[i][2], 280,200, paint);
			   		        	  //canvas.drawBitmap(estimateFace[i][3], 410,200, paint);
			   		        	  	
			   		              //Toast.makeText(context, ("" + result[0]), Toast.LENGTH_SHORT).show();
			   		              //Toast.makeText(context, ("" + result[1]), Toast.LENGTH_SHORT).show();	
			   		              //Toast.makeText(context, ("" + result[2]), Toast.LENGTH_SHORT).show();	
			   		              //Toast.makeText(context, ("" + result[3]), Toast.LENGTH_SHORT).show();	
			   		     	      //Toast.makeText(context, ("face" + result[1]), Toast.LENGTH_SHORT).show();		
			   		     	      //Toast.makeText(context, ("face" + result[2]), Toast.LENGTH_SHORT).show();		
			   		     	      //Toast.makeText(context, ("face" + result[3]), Toast.LENGTH_SHORT).show();
				   		        		
			   		        	 canvas.drawBitmap(estimateFace[i][0], null, faceRect[0], paint);
			   		        	 canvas.drawBitmap(estimateFace[i][1], null, faceRect[1], paint);
			   		        	 canvas.drawBitmap(estimateFace[i][2], null, faceRect[2], paint);
			   		        	 canvas.drawBitmap(estimateFace[i][3], null, faceRect[3], paint);
			   		        	 
			   		        	 	paint.setColor(Color.argb(255, 255, 0, 0));
			   		   		        paint.setStrokeWidth(1); 
			   		   		        canvas.drawRect(faceRect[0], paint);
			   		   		        canvas.drawRect(faceRect[1], paint);
			   		   		        canvas.drawRect(faceRect[2], paint);
			   		   		        canvas.drawRect(faceRect[3], paint);
			   		   		        
			   		   		        
			   	   		     	    canvas.drawLine(345, 135, 330, 120, paint);
			   			        	canvas.drawLine(395, 135, 410, 120, paint);
			   			        	canvas.drawLine(345, 185, 330, 200, paint);
			   			        	canvas.drawLine(395, 185, 410, 200, paint);
				   		     	   //tPaint.setTextSize(12);
			   			        	//canvas.drawText("1", 280, 85, tPaint); // 텍스트 표시
			   			        	//canvas.drawText("2", 410, 85, tPaint); // 텍스트 표시
			   			        	//canvas.drawText("3", 280, 215, tPaint); // 텍스트 표시
			   		        		//canvas.drawText("Captured Face Image", 290, 140, tPaint); // 텍스트 표시
			   		        		//canvas.drawText("Detected Face Image", 290, 230, tPaint); // 텍스트 표시
		   					}
		   					//canvas.drawBitmap(faceImage[i], 300,70, paint);
		   					//canvas.drawBitmap(faceImage[i], 345,135, paint);
		   					
		   					break;
	   		        	case 1: 

	   		        		break;
	   		        	case 2: 

			        		break;
			        		default:
			        			
	   		        }
	   		    }
	   		    
	   		 canvas.drawBitmap(faceImage[0], null, faceRect[4], paint);
	   		 canvas.drawRect(faceRect[4], paint);
	   		}else{
	   				
	   				Toast.makeText(context, "Unable to find face...", Toast.LENGTH_LONG).show();
	   				/*Bitmap ttace = filecontrol.getImageFile(IMAGE_FOLDER, "face2.PNG");
	   				int[] pix = new int[2500];
	   					ttace.getPixels(pix, 0, 50, 0, 0, 50, 50);
	   					int[] result = openCV.faceRecognition(pix, 50, 50);
	   					Bitmap estimateFace[] = new Bitmap[3];
	   		     	   estimateFace[0] = filecontrol.getImageFile(IMAGE_FOLDER, ("face" + result[0] + ".png") );
	   		     	   estimateFace[1] = filecontrol.getImageFile(IMAGE_FOLDER, ("face" + result[1] + ".png") );
	   		     	   estimateFace[2] = filecontrol.getImageFile(IMAGE_FOLDER, ("face" + result[2] + ".png") );
	   		     	   //Toast.makeText(context, ("face" + result[0] + ",jpg"), Toast.LENGTH_LONG).show();		
	   		     	   //Toast.makeText(context, ("face" + result[1] + ",jpg"), Toast.LENGTH_LONG).show();		
	   		     	   //Toast.makeText(context, ("face" + result[2] + ",jpg"), Toast.LENGTH_LONG).show();		
	   		     	   
		        		canvas.drawBitmap(estimateFace[0], 280,70, paint);
		        		canvas.drawBitmap(estimateFace[1], 410,70, paint);
		        		canvas.drawBitmap(estimateFace[2], 280,200, paint);
		        		
		   		        paint.setColor(Color.argb(255, 255, 0, 0));
		   		        paint.setStrokeWidth(1); 
		   		        canvas.drawRect(faceRect[0], paint);
		   		        canvas.drawRect(faceRect[1], paint);
		   		        canvas.drawRect(faceRect[2], paint);
		   		        canvas.drawRect(faceRect[3], paint);
		   		        canvas.drawRect(faceRect[4], paint);
		   		        
	   		     	    canvas.drawLine(345, 135, 330, 120, paint);
			        	canvas.drawLine(395, 135, 410, 120, paint);
			        	canvas.drawLine(345, 185, 330, 200, paint);
			        	canvas.drawLine(395, 185, 410, 200, paint);
		   			*/
	   		}
     }
		/*try {ghksrud 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
     //  invalidate();
       testState = false;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	public void capture(Bitmap bitmap){

		this.bitmap = bitmap;
		takePicture = true;
		
		this.postInvalidate();
	}
	
	public void setBitmap(Bitmap bitmap){
		
		this.bitmap = bitmap;
	}
	
	public void imageTest(){
		
		testState = true;
		
		imgFile = filecontrol.searchFile(IMAGE_FOLDER, searchExtention);
		if(imgFile.length != 0){
		this.bitmap = filecontrol.getImageFile(IMAGE_FOLDER, ("face" + chkCount + ".png") );
		takePicture = true;
			
		chkCount++;
		if(chkCount == imgFile.length) chkCount = 0;
		this.postInvalidate();
		}else{
			Toast.makeText(context, "Need Image Files!!", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void saveFaceImage(){
		if(saveState == true && testState == false){ 
			saveState = false;
			if( filecontrol.saveFaceImage( IMAGE_FOLDER, searchExtention, faceImage[0]) ){
				Toast.makeText(context, "Saved Face Image...", Toast.LENGTH_SHORT).show();
				File[] filePath = filecontrol.searchFile(IMAGE_FOLDER, searchExtention);
				
				if(filePath.length > 3){
					Bitmap trainFace[] = filecontrol.getImageFile(IMAGE_FOLDER, searchExtention);
				    int[][] tPixels = new int [trainFace.length][trainFace[0].getWidth() * trainFace[0].getHeight()];
				   			
				    for(int k = 0; k < trainFace.length; k++){
				   		trainFace[k].getPixels(tPixels[k], 0, imgWidth, 0, 0, imgWidth, imgHeight);
			   		}
			     	if(openCV.learnFace(tPixels, imgWidth, imgHeight, trainFace.length)) Toast.makeText(context, "Learning about All Face Image...", Toast.LENGTH_SHORT).show();
			     	else Toast.makeText(context, "Failed to Save!", Toast.LENGTH_SHORT).show();
			     	
				}else Toast.makeText(context, "Learn at Least Four Face Images!!", Toast.LENGTH_SHORT).show();
			
			}else	Toast.makeText(context, "Not Exist SD card or Fail to Save Face Images!!", Toast.LENGTH_SHORT).show();
		}else Toast.makeText(context, "Input New Face Image!!", Toast.LENGTH_SHORT).show();
	}
	
	public void saveFaceImage(Bitmap imgBitmap){

			if( filecontrol.saveFaceImage( IMAGE_FOLDER, searchExtention, imgBitmap) ){
				Toast.makeText(context, "Saved Face Image...", Toast.LENGTH_SHORT).show();				
			}else	Toast.makeText(context, "Not Exist SD card or Fail to Save Face Images!!", Toast.LENGTH_SHORT).show();
	}
	
}