package dev.imagelab.gallaxyfacedetection;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Log;

/**
 * A CameraSource implementation that obtains its bitmaps via a TCP connection
 * to a remote host on a specified address/port.
 * 
 * @author Tom Gibara
 *
 */

public class SocketCamera extends Thread{

	
	private static final int SOCKET_TIMEOUT = 1000;
	
	private final String address;
	private final int port;

	private final Paint paint = new Paint();

	public SocketCamera(String address, int port) {
		this.address = address;
		this.port = port;
		
		paint.setFilterBitmap(true);
		paint.setAntiAlias(true);
	}
	
	public boolean open() {
		/* nothing to do */
		return true;
	}

	public void close() {
		/* nothing to do */
		
	}
	
	public Bitmap capture() {

		Socket socket = null;
		Bitmap bitmap = null;
        
		try {
			socket = new Socket();
			socket.bind(null);
			socket.setSoTimeout(SOCKET_TIMEOUT);
			socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
			
			//obtain the bitmap
			InputStream in = socket.getInputStream();
			Log.i("LOG_SOCKET", "Socket Enter...");
			bitmap = BitmapFactory.decodeStream(in);
			Log.i("LOG_SOCKET", "obtain the bitmap image");
		} catch (RuntimeException e) {
			Log.i("LOG_SOCKET", "Failed to obtain image over network", e);

		} catch (IOException e) {
			Log.i("LOG_SOCKET", "Failed to obtain image over network", e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				   /* ignore */ 
			}
		}
		return bitmap;
	}
}
	
	