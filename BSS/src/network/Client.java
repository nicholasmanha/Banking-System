package network;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import bss.BSSConsoleUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import enums.*;

public class Client {
	private static OutputHandler outputHandler;
	private static boolean alive = true;
	private static boolean loggedIn;
	private static boolean isProcessing;
	private static String responseMessage;

	public Client(OutputHandler outputHandler) {
		loggedIn = false;
		Client.outputHandler = outputHandler;
	}

	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 1234)) {

			// output, to send TO the server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			
			// input, receive FROM the server
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

			// input handler setup
			InputHandler inputHandler = new InputHandler(objectInputStream);
			Thread inputThread = new Thread(inputHandler);
			inputThread.start();

			// output handler setup
			OutputHandler outputHandler = new OutputHandler(objectOutputStream);
			Thread outputThread = new Thread(outputHandler);
			outputThread.start();
			Client client = new Client(outputHandler);

			// start GUI on thread
			BSSConsoleUI UI = new BSSConsoleUI(client);
			Thread consoleThread = new Thread(UI);
			consoleThread.start();

			// process responses every 200ms
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

	/*
	 * RESPONSE PROCESSING
	 */

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
					responseMessage = "Deposit Successful";
				}
			}
			if (request.getType() == RequestType.WITHDRAW) {
				if (request.getStatus() == Status.SUCCESS) {
					isProcessing = false;
					responseMessage = "Withdraw Successful";
				} else {
					isProcessing = false;
					responseMessage = request.getTexts().get(0);
				}
			}
			if (request.getType() == RequestType.TRANSFER) {
				if (request.getStatus() == Status.SUCCESS) {
					isProcessing = false;
					responseMessage = "Transfer Successful";
				} else {
					isProcessing = false;
					responseMessage = request.getTexts().get(0);
				}
			}
		}
	}

	/*
	 * METHODS FOR GUI
	 */

	public boolean getIsProcessing() {
		return isProcessing;
	}

	public String getResponseMessage() {
		return responseMessage;
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

		outputHandler.enqueueRequest(requests);
	}

	public void createDepositRequest(double amount) {
		isProcessing = true;
		Request depositRequest = new Request(amount, RequestType.DEPOSIT, Status.REQUEST);
		List<Request> requests = new ArrayList<Request>();
		requests.add(depositRequest);
		outputHandler.enqueueRequest(requests);

	}

	public void createWithdrawRequest(double amount) {
		isProcessing = true;
		Request withdrawRequest = new Request(amount, RequestType.WITHDRAW, Status.REQUEST);
		List<Request> requests = new ArrayList<Request>();
		requests.add(withdrawRequest);
		outputHandler.enqueueRequest(requests);

	}

	public void createTransferRequest(int toAccountID, double amount) {
		isProcessing = true;
		ArrayList<String> ID = new ArrayList<String>();
		ID.add(toAccountID + "");
		Request transferRequest = new Request(ID, amount, RequestType.TRANSFER, Status.REQUEST);
		List<Request> requests = new ArrayList<Request>();
		requests.add(transferRequest);

		outputHandler.enqueueRequest(requests);

	}

	public void createLogoutRequest() {
		List<Request> requests = new ArrayList<Request>();
		requests.add(new Request(RequestType.LOGOUT, Status.REQUEST));
		outputHandler.enqueueRequest(requests);
	}

	
}