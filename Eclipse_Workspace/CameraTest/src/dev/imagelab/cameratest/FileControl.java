package dev.imagelab.cameratest;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileControl {

	private String sanitizePath(String path) {
	    if (!path.startsWith("/")) {
	      path = "/" + path;
	    }
	    return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
	}
	
	public File[] searchFile(String path, final String filterStr){
		File dir = new File(sanitizePath(path));
		String[] filelist = dir.list();
		if(filelist == null){
			Log.i("FileControl", "No Search Filelist");
			return null;
		}
		
		File[] files = dir.listFiles();
		FileFilter filter = new FileFilter(){
			public boolean accept(File file){
				String filePath = file.getPath();
				String extention = filePath.substring( filePath.lastIndexOf(".") +1 ).toUpperCase();

				if(extention.equals(filterStr)){return true;}
				else{return false;}
			}
		};
		files = dir.listFiles(filter);
		return files;
	}	
	
	public File[] searchFile(String path,final String[] filterStr){
		File dir = new File(sanitizePath(path));
		String[] filelist = dir.list();

		if(filelist == null){
			Log.i("FileControl", "No Search Filelist");
			return null;
		}

		File[] files = dir.listFiles();
		FileFilter filter = new FileFilter(){
			public boolean accept(File file){
				boolean trueOrFalse = false;
				String filePath = file.getPath();
				String extention = filePath.substring( filePath.lastIndexOf(".") +1 ).toUpperCase();
				
				for( int i = 0; i < filterStr.length; i++){
					if(extention.equals(filterStr[i])){
						trueOrFalse = true;
					}
				}
				return trueOrFalse;
			}
		};
		files = dir.listFiles(filter);
		return files;
	}

	public Bitmap[] getImageFile(String path, String[] filter){
		File[] filePath = null;
		int imageNum = 0;
		
		if( (filePath = searchFile(path, filter) ) == null){			
			return null;
		}
		
		/*String[] faceImage = new String[filePath.length];
		
		for(File file : filePath){
				String inExFileName = file.getName();
				String fileName = inExFileName.substring( inExFileName.lastIndexOf(".") +1 );
				
		}*/
		imageNum = filePath.length;
		Bitmap[] getImage = new Bitmap[imageNum];
		for(int i = 0; i < imageNum; i++){
			getImage[i] = BitmapFactory.decodeFile( filePath[i].getPath() );
		}
		
		//Bitmap getImage = BitmapFactory.decodeFile(pathName)
		//int[] pixels = [filePath.length][];
		//int  listNum = filePath.length;
		//String[] fpath = new String[filePath.length];
		//return filePath.length;
		//for(File file : filepath){
		//	fpath[] = file.getAbsolutePath()
			return getImage; 
		}
		
	public Bitmap getImageFile(String path, String fileName){

		path = sanitizePath(path+fileName);
		return BitmapFactory.decodeFile(path);
	}
	
	public boolean saveFaceImage(String path, String[] filter, Bitmap sBitmap){		   
		FileOutputStream fos;
		File[] filePath = null;
		int imageNum = 0;
		String saveFilePath = null;
		
		if( (filePath = searchFile(path, filter) ) == null){			
			Log.d("Error", "Unable to load file");
		}
		
		imageNum = filePath.length;
		
		saveFilePath = sanitizePath(path+ "face"+imageNum+".png");

		try{

			fos = new FileOutputStream(saveFilePath);
			
			if(fos != null){			
				sBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				Log.i("CameraApiTest","11");
				fos.close();	 
				return true;
			}
			return false;
		}catch(IOException ioe){
			Log.e("saceCamera", "CapturePicture : " + ioe.toString());
			return false;
		}catch(Exception e){
			Log.e("saceCamera", "CapturePicture : " + e.toString());
			return false;
		}
	}
		
	public void saveImagePixels(String path, String name, byte[] data){		   
		String filePath = path + name + ".fpix";
		filePath = sanitizePath(filePath);
		FileOutputStream fos;
		Log.d("CameraApiTest","Path: "+ filePath);
		
		try{

			fos = new FileOutputStream(filePath);
			fos.write(data);  
			fos.flush();  
			fos.close(); 

		}catch(IOException ioe){
			Log.e("saceCamera", "CapturePicture : " + ioe.toString());
		}catch(Exception e){
			Log.e("saceCamera", "CapturePicture : " + e.toString());
		}
	}
	
	public byte[] loadImagePixels(String path, String name){		   
		String filePath = path + name;
		filePath = sanitizePath(filePath);
		byte buffer[] = new byte[2500*4];
		Log.d("CameraApiTest","Path: "+ filePath);
		
		try{

			FileInputStream fis = new FileInputStream(filePath);

			fis.read(buffer);
			
			return buffer;
			
		}catch(IOException ioe){
			Log.e("saceCamera", "CapturePicture : " + ioe.toString());
			return null;
		}catch(Exception e){
			Log.e("saceCamera", "CapturePicture : " + e.toString());
			return null;
		}
	}
	
	/*public int[] loadFaceImage(String path){
		Bitmap faceImage = null;
		int[] pixels = null;
			
		this.imageFilePath = IMAGE_FOLDER + this.fileName + EXTENTION;
		
	    // TODO Do something with the image RAW data.
		  try {  
			  		reader = new FileReader(sanitizePath(this.imageFilePath));
			  stream = new FileOutputStream("/sdcard/IMAGE/" + faceImgae + ".png");
			  faceImage.compress(CompressFormat.PNG, 100, stream);
              stream.flush();
              stream.close();
              
              pixels = new int [2500];
     		faceImage.getPixels(pixels, 0, 50, 0, 0, 50, 50);
     		
		  }catch(Exception ex) {  
     			 ex.printStackTrace();  
     			 return null;
		  }  
		  return pixels;
	}*/
	
	void checkSDCard(String path) throws IOException {
		Log.i("CameraApiTest","9");
		String state = android.os.Environment.getExternalStorageState();
	    if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
	    	//return false;
	       throw new IOException("SD Card is not mounted.  It is " + state + ".");
	    }
	    // make sure the directory we plan to store the recording is exists
	    File directory = new File(path).getParentFile();
	    
	    if (!directory.exists() && !directory.mkdirs()) {
	    
	    	throw new IOException("Path to file could not be created.");
	    }
	}
}