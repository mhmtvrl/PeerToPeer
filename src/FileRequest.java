import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

public class FileRequest implements Runnable {

	private Server app;
	private Socket client;
	private String fileName, header;
	private DataInputStream requestedFile;
	private BufferedReader dis;
	private int fileLength;
	private int byteSent;

	FileRequest(Socket s, Server app) {
		this.app = app;
		client = s;
	}

	@Override
	public void run() {
		requestRead();
		fileOpened();
		constructHeader();
		fileSent();
		/*try {
			fileHodo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	private boolean requestRead() {
		try {
			dis = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			String line;
			int ttl;
			String query;
			String sender;
			String acc = "";
			while ((line = dis.readLine()) != null) {
				if(line.contains("Query"))
				{
					//Query,filename,timetolive,IP
					String tempArr[] = line.split(",");
					fileName = tempArr[1];
					System.out.println(fileName);
					ttl = Integer.parseInt(tempArr[2]);
					sender = tempArr[2];
					ttl--;
					
					/*if(ttl > 0)
					{
						query = "Query,listOfFiles.txt," + ttl + "," + InetAddress.getLocalHost().getHostAddress();
						acc += query;
						Servant.c.connectToNetwork(sender);
						Servant.c.PostFileList("listOfFiles.txt", ttl, InetAddress.getLocalHost().getHostAddress());
					}*/
					
					break;
				}
				acc += line + "\n";
			}
			System.out.println(acc);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	
	private boolean fileOpened() {
		try {
			
			requestedFile = new DataInputStream(new BufferedInputStream(
					new FileInputStream(fileName)));
			fileLength = requestedFile.available();
			
			
		} catch (FileNotFoundException e) {

			if (fileName.equals("filenfound.html")) {
				return false;
			}
			fileName = "filenfound.html";
			if (!fileOpened()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	private void constructHeader() {
		String contentType;

		if ((fileName.toLowerCase().endsWith(".jpg"))
				|| (fileName.toLowerCase().endsWith(".jpeg"))
				|| (fileName.toLowerCase().endsWith(".jpe"))) {
			contentType = "image/jpg";
		} else if ((fileName.toLowerCase().endsWith(".gif"))) {
			contentType = "image/gif";
		} else if ((fileName.toLowerCase().endsWith(".htm"))
				|| (fileName.toLowerCase().endsWith(".html"))) {
			contentType = "text/html";
		} else if ((fileName.toLowerCase().endsWith(".qt"))
				|| (fileName.toLowerCase().endsWith(".mov"))) {
			contentType = "video/quicktime";
		} else if ((fileName.toLowerCase().endsWith(".class"))) {
			contentType = "application/octet-stream";
		} else if ((fileName.toLowerCase().endsWith(".mpg"))
				|| (fileName.toLowerCase().endsWith(".mpeg"))
				|| (fileName.toLowerCase().endsWith(".mpe"))) {
			contentType = "video/mpeg";
		} else if ((fileName.toLowerCase().endsWith(".au"))
				|| (fileName.toLowerCase().endsWith(".snd"))) {
			contentType = "audio/basic";
		} else if ((fileName.toLowerCase().endsWith(".wav"))) {
			contentType = "audio/x-wave";
		} else {
			contentType = "text/plain";
		} // default

		header = "HTTP/1.0 200 OK\n" + "Allow: GET\n" + "MIME-Version: 1.0\n"
				+ "Server: HMJ Basic HTTP Server\n" + "Content-Type: "
				+ contentType + "\n" + "Content-Length: " + fileLength + "\n" + "Tunis" +  "\n";
	}
	
	
	private boolean fileSent() {
		try {
			DataOutputStream clientStream = new DataOutputStream(
					new BufferedOutputStream(client.getOutputStream()));
			clientStream.writeBytes(header);
			int i;
			byteSent = 0;
			while ((i = requestedFile.read()) != -1) {
				clientStream.writeByte(i);
				byteSent++;
			}
			clientStream.flush();
			clientStream.close();

		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private void fileHodo() throws IOException
	{
		 System.out.println("Accepted connection : " + client);
        // File transferFile = new File ("C:\\inetpub\\wwwroot\\" + fileName);
		 File transferFile = new File (fileName);
         byte [] bytearray  = new byte [(int)transferFile.length()];
         FileInputStream fin = new FileInputStream(transferFile);
         BufferedInputStream bin = new BufferedInputStream(fin);
         bin.read(bytearray,0,bytearray.length);
         OutputStream os = client.getOutputStream();
         System.out.println("Sending Files...");
         os.write(bytearray,0,bytearray.length);
         os.flush();
         client.close();
         System.out.println("File transfer complete");   
	}
}
