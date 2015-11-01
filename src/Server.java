import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server implements Runnable {
	private ServerSocket ss;
	private Thread runner = null;
	
	public Server()
	{
		runner = new Thread(this);
		runner.start();
	}
	

	@Override
	public void run() 
	{
		Socket s;
		try {
			ss = new ServerSocket(4911);
			while(true)
			{
				s = ss.accept();
				PrintStream pr = new PrintStream(s.getOutputStream());
				pr.println("Listenning again ** port: 8080 \n");
				new Thread(new FileRequest(s, this)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
