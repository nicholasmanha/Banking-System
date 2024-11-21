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
		Bank bank;

		public ClientHandler(Bank bank, Socket socket) {
			this.bank = bank;
			this.clientSocket = socket;
		}

		public void run() {
			UserType userType;
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
				if (request.getType() == RequestType.LOGIN && request.getStatus() == Status.REQUEST) {
					System.out.println("login request recieved");
					String username = request.getTexts().get(0);
					String password = request.getTexts().get(1);
					System.out.println(username + ", " + password);
					List<Request> responses = new ArrayList<Request>();
					responses.add(new Request(Requester.USER, RequestType.LOGIN, Status.SUCCESS));
					outHandler.enqueueRequest(responses);
				}
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
