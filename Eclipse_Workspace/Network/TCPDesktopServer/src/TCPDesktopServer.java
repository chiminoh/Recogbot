import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPDesktopServer implements Runnable{
	
    public static final String SERVERIP = "127.0.0.1";
    public static final int SERVERPORT = 4444;
         
    public void run() {
        try {
       	 
       	 //InetAddress serverAddr = InetAddress.getByName("192.168.0.140");//TCPServer.SERVERIP
       	 
       	 System.out.println("C: Connecting...");
       	 Socket socket = new Socket("127.0.0.1", 4444);
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
    	Thread desktopServerThread = new Thread(new TCPDesktopServer());
    	desktopServerThread.start();
    }
}