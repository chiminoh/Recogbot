package test.campoenent.sockettest;
import android.app.Activity;
import android.os.Bundle;

public class SocketTest extends Activity {
   /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
       super.onCreate(icicle);
       setContentView(R.layout.main);       

       Thread Thread = new Thread(new TCPClient());
     
       Thread.start(); 
	}
}