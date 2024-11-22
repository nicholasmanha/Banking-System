package bss;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

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
	private static OutHandler outHandler;
	private static boolean alive = true;
	private static boolean loggedIn;
	private static boolean isProcessing;

	public Client(OutHandler outHandler) {
		loggedIn = false;
		Client.outHandler = outHandler;
	}

	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 1234)) {

			// output, to send TO the server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

			InputHandler inputHandler = new InputHandler(objectInputStream);
			Thread inputThread = new Thread(inputHandler);
			inputThread.start();

			OutHandler outHandler = new OutHandler(objectOutputStream);

			Thread outputThread = new Thread(outHandler);
			outputThread.start();

			Client client = new Client(outHandler);

			BSSConsoleUI UI = new BSSConsoleUI(client);
			Thread consoleThread = new Thread(UI);
			consoleThread.start();
			while (alive) {
				List<Request> req = inputHandler.getNextRequest();
				if (req != null) {

					processResponse(req);
				}

				Thread.sleep(200);
			}

			System.out.println("Closing socket");

			socket.close();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void processResponse(List<Request> req) {
		for (Request request : req) {
			if (request.getType() == RequestType.LOGIN) {
				if (request.getStatus() == Status.SUCCESS) {
					loggedIn = true;
				} else {
					System.out.println("login failed");
				}
			}
			if (request.getType() == RequestType.LOGOUT) {
				if (request.getStatus() == Status.SUCCESS) {
					System.out.println("logging out");
					alive = false;
					loggedIn = false;
				}
			}
			if (request.getType() == RequestType.DEPOSIT) {
				if (request.getStatus() == Status.SUCCESS) {
					isProcessing = false;
				}
			}
			if (request.getType() == RequestType.WITHDRAW) {
				if (request.getStatus() == Status.SUCCESS) {
					System.out.println("withdraw successful");
				} else {
					System.out.println(request.getTexts().get(0));
				}
			}
		}
	}

	public boolean getIsProcessing() {
		return isProcessing;
	}

	public boolean getLoggedIn() {
		return loggedIn;
	}

	public void createLoginRequest(String username, String password) {
		ArrayList<String> userAndPass = new ArrayList<String>();
		userAndPass.add(username);
		userAndPass.add(password);
		Request loginRequest = new Request(userAndPass, RequestType.LOGIN, Status.REQUEST);
		List<Request> requests = new ArrayList<Request>();
		requests.add(loginRequest);

		outHandler.enqueueRequest(requests);
	}

	public void createDepositRequest(double amount) {
		isProcessing = true;
		Request depositRequest = new Request(amount, RequestType.DEPOSIT, Status.REQUEST);
		List<Request> requests = new ArrayList<Request>();
		requests.add(depositRequest);
		outHandler.enqueueRequest(requests);

	}

	public void createWithdrawRequest(double amount) {
		Request withdrawRequest = new Request(amount, RequestType.WITHDRAW, Status.REQUEST);
		List<Request> requests = new ArrayList<Request>();
		requests.add(withdrawRequest);
		outHandler.enqueueRequest(requests);

	}
	
	public void createTransferRequest(int toAccountID, double amount) {
		Request withdrawRequest = new Request(amount, RequestType.WITHDRAW, Status.REQUEST);
		List<Request> requests = new ArrayList<Request>();
		requests.add(withdrawRequest);
		outHandler.enqueueRequest(requests);

	}

	public void createLogoutRequest() {
		List<Request> requests = new ArrayList<Request>();
		requests.add(new Request(RequestType.LOGOUT, Status.REQUEST));
		outHandler.enqueueRequest(requests);
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

					Thread.sleep(200);
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

}
