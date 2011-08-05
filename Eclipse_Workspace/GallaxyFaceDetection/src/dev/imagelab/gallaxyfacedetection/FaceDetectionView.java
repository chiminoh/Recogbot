package dev.imagelab.gallaxyfacedetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.Log;
import android.view.View;

public class FaceDetectionView extends View{

	private FaceDetector.Face[] faces;     // 인식된 얼굴의 정보가 저장될 곳
	private Rect TopView;                  // 320*240 사이즈의 캠처 이미지를 그릴 뷰 지정
	private FaceDetector detector;         // Face Detector 선언
	public Bitmap bitmap;                 // 캡처 이미지가 저장될 곳입니다.
	
	private final Paint paint;             // 물감이고요~
	private final String SERVERIP;         // 자신의 PC IP 입니다.
	private final int SERVERPORT;          // 포트입니다. 서버가 9889이니 9889로 해주세요
	
	public FaceDetectionView(Context context) {
		super(context);
		SERVERIP = "192.168.10.104";        
		SERVERPORT = 9889;                   
		
		faces = new FaceDetector.Face[5];   // 최대 얼굴 인식 개수는 5개
		TopView = new Rect(0, 0, 640, 480);   // 이미지를 보여줄 뷰 사이즈는 그래도 320*240
		
		paint = new Paint();
	    paint.setColor(Color.argb(255, 255, 0, 0)); 
	    paint.setStyle(Style.STROKE); 
	    paint.setAntiAlias(true);
	    paint.setStrokeWidth(3); 
	    
		SocketCamera CapImage = new SocketCamera(SERVERIP, SERVERPORT);
		if(!CapImage.open()){Log.d("TCP", "Failed to obtain image over network");}
		bitmap = CapImage.capture(); //capture the frame onto the canvas
		
		// 디텍터 가동!
		Log.i("LOG_TAG", "build faces...."); 
	    detector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), faces.length);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		SocketCamera CapImage = new SocketCamera(SERVERIP, SERVERPORT);
		if(!CapImage.open()){Log.d("TCP", "Failed to obtain image over network");}
		bitmap = CapImage.capture(); //capture the frame onto the canvas
		CapImage.close();
		
		Log.i("information", "Bitmap Config Change!!");

		bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);   
		canvas.drawBitmap(bitmap, null, TopView, paint);     
		
		int numFaces = detector.findFaces(bitmap, faces);    
		Log.i("information", "faces num : " + numFaces); 
		
		if (numFaces > 0) {						// 만일 하나의 얼굴이라도 검출이 된다면	    
		    for (int i = 0; i < numFaces; i++) {
		        Face face = faces[i];          
		        PointF midPoint = new PointF(0, 0);  
		        face.getMidPoint(midPoint); 	// 두 눈 사이의 가운데 점
		        float eyesDistance = face.eyesDistance(); 	// 두 눈 사이의 거리
		       
		        Path circle;					// 얼굴 위치에 동그라미를 그려줍니다.
		        	
		        circle = new Path();
		        circle.addCircle(midPoint.x*2, (midPoint.y+eyesDistance/2)*2, (eyesDistance*1.7F)*2, Direction.CW);
		        canvas.drawPath(circle, paint);                
		    }
		}
		invalidate();     // call onDraw(in UI thread)
	}
}
