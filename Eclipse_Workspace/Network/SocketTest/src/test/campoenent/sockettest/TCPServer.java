package test.campoenent.sockettest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class TCPServer implements Runnable {

	public static final String SERVERIP = "168.131.153.36";
	public static final int SERVERPORT = 4444;
	     
	public void run() {
	     try {
	    	 Log.d("TCP", "C: Connecting...");
	          
	         ServerSocket serverSocket = new ServerSocket(SERVERPORT);
	         
	         while (true) {
	        	  
	        	  Socket client = serverSocket.accept();
	        	  Log.d("TCP", "C: Received... ");

	        	  try {
	                  BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	                  String str = in.readLine();
	                  Log.d("TCP", "C: Received: '" + str + "'");
	                } catch(Exception e) {
	                	Log.d("TCP", "C: Error2");
	                    e.printStackTrace();
	                } finally {
	                	client.close();
	                	Log.d("TCP", "C: Done.");
	                }

	         }
	          
	     } catch (Exception e) {
	         System.out.println("C: Error");
	         e.printStackTrace();
	     }
	}
}
