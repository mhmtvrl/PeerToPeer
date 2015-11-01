import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class Client {
	
	private Socket s;
	
	public Client()
	{
		FileWriter file;
		try {
			file = new FileWriter("IPAdresses.txt");
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getDirectory();
	}
	
	public void getDirectory()
	{
		try {
			FileWriter file = new FileWriter("listOfFiles"+InetAddress.getLocalHost().getHostAddress()+".txt");
			String newLine = System.getProperty("line.separator");
			File files[] = new File("C:/inetpub/wwwroot").listFiles();
			InetAddress adr = InetAddress.getLocalHost();
			file.write(adr.getHostAddress() + ",");
			
			for(int i = 0; i < files.length - 1; i++)
			{
				file.write(files[i].getName() + ",");
			}
			file.write(files[files.length - 1].getName());
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void appendFile(String s, String s2)
	{
		Scanner sc;
		ArrayList<String> lines = new ArrayList<String>();
		try {
			File f = new File(s2);
			sc = new Scanner(f);
			String line;
			while(sc.hasNextLine())
			{
				line = sc.nextLine();
				lines.add(line);
			}
			FileWriter file2 = new FileWriter(f.getName(), true);
			if(!lines.contains(s))
			{
				file2.append("\n" + s);
			}
			file2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean connect(String host)
	{
		try {
			s = new Socket(host, 4911);
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			System.out.println("connect func");
			return false;
		}
		this.appendFile(host, "IPAdresses.txt");
		return true;
	}
	
	public boolean connectToNetwork(String host)
	{
		File file = new File("IPAdresses.txt");
		try {
			Scanner sc = new Scanner(file);
			String line;
			while(sc.hasNextLine())
			{
				line = sc.nextLine();
				if(connect(line))
					break;
			}
			
			return connect(host);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("böyle bu");
			return false;
			
		}
	}
	
	
	public void PostFileList(String fileName, int ttl, String host)
	{
		try {

			String request = "POST / HTTP/1.0\n" + 
					"Connection: Keep-Alive\n" +
					"User-Agent: CS328-Servant\n" +
					"Accept-Language: en\n" + 
					"Content-type: application/x-www-form-urlencoded\n" +
					"Content-length:\n" + 
					"Query," + fileName + "," + ttl + "," + host;
			PrintWriter PWout1 = new PrintWriter ( new OutputStreamWriter( s.getOutputStream() ) );
	        BufferedReader BRin = new BufferedReader ( new InputStreamReader( s.getInputStream () ) );
	        PWout1.println(request);
	        FileWriter file = new FileWriter(fileName);

            PWout1.flush();
            //System.out.println(BRin.readLine());
            String line;
            while((line = BRin.readLine()) != null)
            {
            	System.out.println(line);
            	if(line.contains(","))
            	{
            		appendFile(line, "listOfFiles.txt");
            		break;
            	}
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getFile(String fileName)
	{
		int ttl = 1;
		try {
			FileWriter file = new FileWriter(fileName);
			String newLine = System.getProperty("line.separator");
			String request = "POST / HTTP/1.0\n" + 
					"Connection: Keep-Alive\n" +
					"User-Agent: CS328-Servant\n" +
					"Accept-Language: en\n" + 
					"Content-type: application/x-www-form-urlencoded\n" +
					"Content-length:\n" + 
					"Query," + fileName + "," + ttl + "," + InetAddress.getLocalHost().getHostAddress();
			PrintWriter PWout1 = new PrintWriter ( new OutputStreamWriter( s.getOutputStream() ) );
	        BufferedReader BRin = new BufferedReader ( new InputStreamReader( s.getInputStream () ) );
	        PWout1.println(request);
            PWout1.flush();
            
            String line;
            String line2;
            while((line = BRin.readLine()) != null)
            {
            	System.out.println(line);
            	if(line.contains("Tunis"))
            	{
            		System.out.println("whilenin ustunde");
            		 while((line2 = BRin.readLine()) != null)
            		 {
            			 file.write(line2 + newLine);
            			 System.out.println(line2);
            		 }
            		break;
            		
            	}
            }
           
            file.close();
           
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}