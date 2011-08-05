package test.campoenent.sockettest;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;

public class TCPClient implements Runnable {

	
    public void run() {
         try {
        	 
        	 //InetAddress serverAddr = InetAddress.getByName("127.0.0.1");//TCPServer.SERVERIP
        	 
        	 Log.d("TCP", "C: Connecting...");
        	 Socket socket = new Socket(TCPServer.SERVERIP, TCPServer.SERVERPORT);
        	 String message = "Hello from Client";
		     try {
		    	 Log.d("TCP", "C: Sending: '" + message + "'");
		    	 PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
		    	 
		    	 out.println(message);
		    	 Log.d("TCP", "C: Sent.");
	             Log.d("TCP", "C: Done.");
		    	 
             } catch(Exception e) {
                 Log.e("TCP", "C: Error", e);
		      } finally {
		        socket.close();
		      }
         } catch (Exception e) {
              Log.e("TCP", "C: Error", e);
         }
    }
}
