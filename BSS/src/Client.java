import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;



public class Client {

	public static void main(String[] args) {
		try (Socket socket = new Socket("ip goes here", 1234)) {
			// output, to send TO the server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			
			// input, to recieve FROM the server
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
	        
	        System.out.println("Closing socket");
	        socket.close();
	        
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
