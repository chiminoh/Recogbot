import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;


public class TCPServer {
	private static int port = 2222;
	private Vector handlers;
	
	public TCPServer (int port){
		try {
			ServerSocket server = new ServerSocket(port);
			handlers = new Vector();
			System.out.println("Server is Ready!!");		
			while(true){
				Socket client = server.accept();
				ChatHandler c = new ChatHandler(this, client);
				c.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Object getHandler(int index){
		return handlers.elementAt(index);
	}
	public void register(ChatHandler c){
		handlers.addElement(c);
	}
	public void unRegister(Object o){
		handlers.removeElement(0);
	}
	
	public void broadcast (String message){
		synchronized(handlers){
			int n = handlers.size();
			for(int i = 0; i < n; i++){
				ChatHandler c = (ChatHandler) handlers.elementAt(i);
				try{
					c.println(message);
					System.out.println("Message Out!");
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
	
	public static void main (String args[]){
		TCPServer server = new TCPServer(port);
		//while(true){
		//	Scanner sc = new Scanner(System.in);
		//	String message = sc.next();
		//	server.broadcast(message);
		//}
	}
}
