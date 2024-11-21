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
import java.util.concurrent.ConcurrentLinkedQueue;

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
				System.out.println("New client connected" + client.getInetAddress().getHostAddress());

				// create a new thread object
				ClientHandler clientSock = new ClientHandler(bank, client);

				// This thread will handle the client
				// separately
				new Thread(clientSock).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	

	private static class ClientHandler implements Runnable {
		private static OutHandler outHandler;
		private final Socket clientSocket;
		private static Bank bank;
		private static UserType userType;
		private static boolean loggedIn;
		private static ATM atm;
		private static Session session;
		public ClientHandler(Bank bank, Socket socket) {
			atm = new ATM();
			this.loggedIn = false;
			this.bank = bank;
			this.clientSocket = socket;
		}

		public void run() {
			Teller firstTeller = new Teller("password");
			bank.addTeller(firstTeller);
			
			Account testAccount = firstTeller.createAccount("123");
			bank.addAccount(testAccount);
			// for debugging purposes
			for (Account account : bank.getAccounts()) {
				System.out.println(account.getAccountID());
			}

			try {
				
				
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

				ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

				InputHandler inputHandler = new InputHandler(objectInputStream);
				Thread inputThread = new Thread(inputHandler);
				inputThread.start();

				OutHandler outHandler = new OutHandler(objectOutputStream);
				ClientHandler.outHandler = outHandler;
				Thread outputThread = new Thread(outHandler);
				outputThread.start();

				while (true) {
					List<Request> req = inputHandler.getNextRequest();
					if (req != null) {

						System.out.println("Received: " + req);
						processRequest(req);
					}

					Thread.sleep(1000);
				}

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();

			}
		}
		private static void processRequest(List<Request> req) {
			for (Request request : req) {
				RequestType type = request.getType();
				if(type == RequestType.LOGIN && request.getStatus() == Status.REQUEST) {
					doLogin(request);
				}
				if(type == RequestType.LOGOUT && request.getStatus() == Status.REQUEST) {
					doLogout(request);
				}
				if(type == RequestType.DEPOSIT) {
					if(loggedIn == true) {
						session.getAccount().deposit(request.getAmount());
						System.out.println("new balance: " + session.getAccount().getAmount());
					}
					
				}
			}
		}
		
		private static void doLogin(Request request) {
			System.out.println("login request recieved");
			String username = request.getTexts().get(0);
			String password = request.getTexts().get(1);
			System.out.println(username + ", " + password);
			int usernameInt = Integer.parseInt(username);
			
			userType = determineUserType(bank, usernameInt);
			
			if(userType == UserType.Customer) {
				Account acc = bank.findAccount(usernameInt);
				if(acc.checkCredentials(usernameInt, password)) {
				
					List<Request> loginResponses = new ArrayList<>();
					Request loginResponse = new Request(Requester.USER, RequestType.LOGIN, Status.SUCCESS);
					loginResponses.add(loginResponse);
					
					outHandler.enqueueRequest(loginResponses);
					loggedIn = true;
					System.out.println("this is a customer");
					
					session = atm.logIn(acc);
				}
				else {
					List<Request> loginResponses = new ArrayList<>();
					Request loginResponse = new Request(Requester.USER, RequestType.LOGIN, Status.FAILURE);
					loginResponses.add(loginResponse);
					
					outHandler.enqueueRequest(loginResponses);
				}
			}
			else if(userType == UserType.Teller) {
				Teller teller = bank.findTeller(usernameInt);
				
			}
			else {
				List<Request> loginResponses = new ArrayList<>();
				Request loginResponse = new Request(Requester.USER, RequestType.LOGIN, Status.FAILURE);
				loginResponses.add(loginResponse);
				
				outHandler.enqueueRequest(loginResponses);
			}
		}
		
		private static void doLogout(Request request) {
			if(loggedIn) {
				atm.logOut();
				List<Request> logoutResponses = new ArrayList<>();
				Request logoutResponse = new Request(RequestType.LOGOUT, Status.SUCCESS);
				logoutResponses.add(logoutResponse);
				
				outHandler.enqueueRequest(logoutResponses);
			}
		}
		
		private static UserType determineUserType(Bank bank, int userID) {

			Teller teller;
			Account acc;
			acc = bank.findAccount(userID);
			if (acc == null) {
				teller = bank.findTeller(userID);
				if (teller == null) {
					System.out.println("account undefined");
					return UserType.Undefined;
				}
				return UserType.Teller;
			}
			return UserType.Customer;
		}

	}
	private static class OutHandler implements Runnable {
		private final ObjectOutputStream outputStream;
		private final ConcurrentLinkedQueue<List<Request>> requestQueue;
		private boolean running = true;

		public OutHandler(ObjectOutputStream out) {
			this.outputStream = out;
			this.requestQueue = new ConcurrentLinkedQueue<>();
		}

		public void enqueueRequest(List<Request> requests) {
			requestQueue.add(requests);
		}

		public void run() {
			while (running) {
				List<Request> requests = requestQueue.poll();
				if (requests != null) {
					try {
						
						outputStream.writeObject(requests);
						outputStream.flush();
						System.out.println("sent message");
					} catch (IOException e) {
						running = false;
					}
				}

				try {

					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private static class InputHandler implements Runnable {
		private final ObjectInputStream inputStream;
		private final ConcurrentLinkedQueue<List<Request>> requestQueue;
		private boolean running = true;

		public InputHandler(ObjectInputStream in) {
			this.inputStream = in;
			this.requestQueue = new ConcurrentLinkedQueue<>();
		}

		public void run() {
			while (running) {
				try {
					List<Request> requests = (List<Request>) inputStream.readObject();
					if (requests != null) {
						requestQueue.add(requests);
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
					running = false;
				}

				try {

					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}

		public void stop() {
			running = false;
		}

		public List<Request> getNextRequest() {
			return requestQueue.poll();
		}
	}
}
