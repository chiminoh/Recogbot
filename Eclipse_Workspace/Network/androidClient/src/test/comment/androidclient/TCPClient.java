package test.comment.androidclient;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TCPClient extends View implements Runnable { 

    
    public TCPClient(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		Thread thread = new Thread(this);
		thread.start();
		
		// TODO Auto-generated constructor stub
	}

	public void run() { 
         
		try { 
            
           //InetAddress serverAddr = InetAddress.getByName("168.131.153.42"); 
            
        	 Log.d("TCP", "C: Connecting..."); 
           Socket socket = new Socket("168.131.153.42", 9889);
           String message = "Hello from Client";
           String message1 = "Hello from Client1";
           String message2 = "Hello from Client2";
               try { 
            	Log.d("TCP", "C: Sending: '" + message + "'");  
                PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true); 
                
                out.println(message2);
                out.println(message1);
                out.println(message2);
                
                Log.d("TCP", "C: Sent."); 
                Log.d("TCP", "C: Done."); 
                
             } catch(Exception e) { 
            	 Log.e("TCP", "S: Error", e); 
                } finally { 
                  socket.close(); 
                } 
         } catch (Exception e) { 
        	 Log.e("TCP", "C: Error", e);  
         }
         }
}