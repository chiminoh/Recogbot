import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient implements Runnable{
	
    public static final String SERVERIP = "168.131.153.235";
    public static final int SERVERPORT = 4444;
    private static final int SOCKET_TIMEOUT = 1000;
    
    public void run() {
        try {
       	 
       	 //InetAddress serverAddr = InetAddress.getByName("192.168.0.140");//TCPServer.SERVERIP
       	 
       	 System.out.println("C: Connecting...");
       	 
       	Socket socket = new Socket();
       	socket = new Socket();
		socket.bind(null);
		socket.setSoTimeout(SOCKET_TIMEOUT);
		socket.connect(new InetSocketAddress("168.131.153.42", 4444), SOCKET_TIMEOUT);
       	 
       	 
       	 //Socket socket = new Socket(SERVERIP, 4444);
       	 String message = "Hello from Client";
		     try {
		    	 System.out.println("C: Sending: '" + message + "'");
		    	 PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
		    	 
		    	 out.println(message);
		    	 System.out.println("C: Sent.");
		    	 System.out.println("C: Done.");
		    	 
            } catch(Exception e) {
            	System.out.println("S: Error");
		      } finally {
		        socket.close();
		      }
        } catch (Exception e) {
        	System.out.println("C: Error");
        }
    }
    
    public static void main (String a[]) {
    	Thread clientThread = new Thread(new TCPClient());
    	clientThread.start();
    }
}