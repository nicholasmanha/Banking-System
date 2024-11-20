package bss;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import requests.Request;
import enums.*;



public class Client {
	public static void main(String[] args) {
		try (Socket socket = new Socket("192.168.56.1", 1234)) {
			// output, to send TO the server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			
			// input, to recieve FROM the server
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
	        
			BSSUserInterface bssUI = new BSSConsoleUI();
	        
	        ArrayList<String> userAndPass = bssUI.login();
			
			
			List<Request> loginMessages = new ArrayList<>();
			Request loginMessage = new Request(userAndPass, RequestType.LOGIN, Status.REQUEST);
			loginMessages.add(loginMessage);

			// send the login request
			System.out.println("Sending Login Request...");
	        objectOutputStream.writeObject(loginMessages);
			
	        System.out.println("Closing socket");
	        socket.close();
	        
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
