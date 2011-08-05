import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ChatHandler extends Thread{
	private Socket s;
	private BufferedReader i;
	private PrintWriter o;
	private TCPServer server;
	
	public ChatHandler(TCPServer server, Socket s) throws IOException{
		this.s = s;
		this.server = server;
		InputStream ins = s.getInputStream();
		OutputStream os = s.getOutputStream();
		i = new BufferedReader(new InputStreamReader(ins));
		o = new PrintWriter(new OutputStreamWriter(os), true);
	}
	
	public void run(){
		String data = "";
		try{
			server.register(this);
			System.out.println(s.getLocalAddress()+" 에서 접근...");
			while(true){
				//data = i.readLine();
				Scanner sc = new Scanner(System.in);
				data = sc.next();
				broadcast(s.getLocalAddress() + " - " + data);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		server.unRegister(this);
		broadcast(s.getLocalAddress() + " 접속 종료!!");
		
		try{
			i.close();
			o.close();
			s.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	protected void println(String message){
		o.println(message);
	}
	
	protected void broadcast (String message){
		server.broadcast(message);
	}
}
