package bss;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import enums.*;
import requests.Request;

public class Server {
	
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			// server is listening on port 1234
			server = new ServerSocket(1234);
			server.setReuseAddress(true);
			Bank bank = new Bank();
			// running infinite loop for getting
			// client request
			while (true) {

				// socket object to receive incoming client
				// requests
				Socket client = server.accept();

				// Displaying that new client is connected
				// to server
				System.out.println("New client connected"
								+ client.getInetAddress()
										.getHostAddress());

				// create a new thread object
				ClientHandler clientSock
					= new ClientHandler(bank, client);

				// This thread will handle the client
				// separately
				new Thread(clientSock).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (server != null) {
				try {
					server.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		Bank bank;
		public ClientHandler(Bank bank, Socket socket)
		{
			this.bank = bank;
			this.clientSocket = socket;
		}
		public void run() {
			UserType userType;
			PrintWriter out = null;
			BufferedReader in = null;
			// for debugging purposes
			for(Account account : bank.getAccounts()) {
				System.out.println(account.getAccountID());
			}
			try {
				ATM atm = new ATM();
				// get the outputstream of client
				OutputStream outputStream = clientSocket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

				// get the inputstream of client
				InputStream inputStream = clientSocket.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				Teller firstTeller = new Teller("password");
				bank.addTeller(firstTeller);
				
				Account testAccount = firstTeller.createAccount("123");
				bank.addAccount(testAccount);
				
				try {
					
					List<Request> loginRequestList = (List<Request>) objectInputStream.readObject();
					
					if(loginRequestList.get(0).getType()==RequestType.LOGIN && loginRequestList.get(0).getStatus()==Status.REQUEST) {
						
						Request loginRequest = loginRequestList.get(0);
						
						int requestUserID = Integer.parseInt(loginRequest.getTexts().get(0));
						
						userType = determineUserType(bank, requestUserID);
						
						if(userType == UserType.Customer) {
							Account acc = bank.findAccount(requestUserID);
							if(acc.checkCredentials(Integer.parseInt(loginRequestList.get(0).getTexts().get(0)), loginRequestList.get(0).getTexts().get(1))) {
							
								List<Request> loginResponses = new ArrayList<>();
								Request loginResponse = new Request(Requester.USER, RequestType.LOGIN, Status.SUCCESS);
								loginResponses.add(loginResponse);
								
								objectOutputStream.writeObject(loginResponses);
								System.out.println("this is a customer");
								Session session = atm.logIn(acc);
							}
							else {
								List<Request> loginResponses = new ArrayList<>();
								Request loginResponse = new Request(Requester.USER, RequestType.LOGIN, Status.FAILURE);
								loginResponses.add(loginResponse);
								
								objectOutputStream.writeObject(loginResponses);
							}
						}
						else if(userType == UserType.Teller) {
							Teller teller = bank.findTeller(requestUserID);
							System.out.println("this is a teller");
							
						}
						else {
							List<Request> loginResponses = new ArrayList<>();
							Request loginResponse = new Request(Requester.USER, RequestType.LOGIN, Status.FAILURE);
							loginResponses.add(loginResponse);
							
							objectOutputStream.writeObject(loginResponses);
						}
						
					}
			        System.out.println("Closing socket " + clientSocket.getRemoteSocketAddress());
			        clientSocket.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
						clientSocket.close();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static UserType determineUserType(Bank bank, int userID) {
		
		Teller teller;
		Account acc; 
		acc = bank.findAccount(userID);
		if(acc == null) {
			teller = bank.findTeller(userID);
			if(teller == null) {
				System.out.println("account undefined");
				return UserType.Undefined;
			}
			return UserType.Teller;
		}
		return UserType.Customer;
	}
	
}
