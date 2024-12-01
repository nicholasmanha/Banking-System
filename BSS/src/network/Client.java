package network;

import bss.BSSConsoleUI;
import enums.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;
	private boolean alive = true;
	private boolean loggedIn = false;
	private boolean isProcessing = false;
	private String responseMessage;
	private UserType userType;

	public Client(InputHandler inputHandler, OutputHandler outputHandler) {
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
	}

	public static void main(String[] args) {
		final String HOST = "localhost";
		final int PORT = 1234;

		Socket socket = null;
		try {
			// Establishing the socket connection
			socket = new Socket(HOST, PORT);

			// Output stream setup
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			// Input stream setup
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

			// Handlers
			InputHandler inputHandler = new InputHandler(objectInputStream);
			OutputHandler outputHandler = new OutputHandler(objectOutputStream);

			// Threads for handlers
			Thread inputThread = new Thread(inputHandler);
			Thread outputThread = new Thread(outputHandler);
			inputThread.start();
			outputThread.start();

			// Create client instance
			Client client = new Client(inputHandler, outputHandler);

			// Start GUI
			BSSConsoleUI UI = new BSSConsoleUI(client);
			Thread consoleThread = new Thread(UI);
			consoleThread.start();

			// Process server responses
			while (client.alive) {
				List<Request> req = inputHandler.getNextRequest();
				if (req != null) {
					client.processResponse(req);
				}
				Thread.sleep(200);
			}

			System.out.println("Closing socket...");

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			// Ensuring the socket is closed, even if an exception occurred
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace(); // Handle error closing the socket
			}
		}
	}

	/*
	 * RESPONSE PROCESSING Added empty checks
	 */
	private void processResponse(List<Request> req) {
		isProcessing = false;
		for (Request request : req) {
			RequestType requestType = request.getType();
			switch(requestType) {
			case LOGIN:
				if (request.getStatus() == Status.SUCCESS) {
					loggedIn = true;
					responseMessage = "Login Successful";
					userType = request.getUserType();
				} else {
					System.out.println("Login failed");
				}
				break;
			case LOGOUT:
				if (request.getStatus() == Status.SUCCESS) {
					System.out.println("Logging out...");
					createLogoutRequest(); // Graceful shutdown
				}
				break;
			case DEPOSIT:
				if (request.getStatus() == Status.SUCCESS) {
					responseMessage = "Deposit Successful";
				}
				if (request.getStatus() == Status.FAILURE) {
					responseMessage = request.getTexts().get(0);
				}
				break;
			case WITHDRAW:
				if (request.getStatus() == Status.SUCCESS) {
					responseMessage = "Withdraw Successful";
				} else if (request.getTexts() != null && !request.getTexts().isEmpty()) {
					responseMessage = request.getTexts().get(0);
				}
				break;
			case TRANSFER:
				if (request.getStatus() == Status.SUCCESS) {
					responseMessage = "Transfer Successful";
				} else if (request.getStatus() == Status.FAILURE) {
					responseMessage = request.getTexts().get(0);
				}
				break;
			case FREEZE:
				if (request.getStatus() == Status.SUCCESS) {
					responseMessage = "Freeze Successful";
				}
				break;
			default:
				break;
			}
		}
	}

	/*
	 * METHODS FOR GUI
	 */
	public UserType getUserType() {
		return userType;
	}

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
		ArrayList<String> userAndPass = new ArrayList<>();
		userAndPass.add(username);
		userAndPass.add(password);
		sendRequest(userAndPass, RequestType.LOGIN, Status.REQUEST);
	}

	public void createDepositRequest(double amount) {
		isProcessing = true;
		sendRequest(amount, RequestType.DEPOSIT, Status.REQUEST);
	}

	public void createWithdrawRequest(double amount) {
		isProcessing = true;
		sendRequest(amount, RequestType.WITHDRAW, Status.REQUEST);
	}

	public void createTransferRequest(int toAccountID, double amount) {
		isProcessing = true;
		ArrayList<String> ID = new ArrayList<>(Arrays.asList(toAccountID + ""));
		sendRequest(ID, amount, RequestType.TRANSFER, Status.REQUEST);
	}

	public void createFreezeRequest(int acc_ID) {
		isProcessing = true;
		ArrayList<String> ID = new ArrayList<>(Arrays.asList(acc_ID + ""));
		sendRequest(ID, RequestType.FREEZE, Status.REQUEST);
	}

	public void createEnterAccountRequest(int acc_ID) {
		isProcessing = true;
		ArrayList<String> ID = new ArrayList<>(Arrays.asList(acc_ID + ""));
		sendRequest(ID, RequestType.ENTER, Status.REQUEST);
	}
	
	public void createLogoutRequest() {
		sendRequest(RequestType.LOGOUT, Status.REQUEST);

		// Shutdown
		try {
			System.out.println("Stopping Handlers...");
			inputHandler.stop();
			outputHandler.stop();
			alive = false;

			// Wait for threads to finish
			Thread inputThread = new Thread(inputHandler);
			Thread outputThread = new Thread(outputHandler);
			inputThread.join();
			outputThread.join();

			// Close the socket and streams
			if (inputHandler.getInputStream() != null) {
				inputHandler.getInputStream().close();
			}
			if (outputHandler.getOutputStream() != null) {
				outputHandler.getOutputStream().close();
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void sendRequest(RequestType requestType, Status status) {
		List<Request> responses = new ArrayList<>();
		Request response = new Request(requestType, status);
		responses.add(response);
		outputHandler.enqueueRequest(responses);
	}

	private void sendRequest(ArrayList<String> messages, RequestType requestType, Status status) {
		List<Request> responses = new ArrayList<>();
		Request response = new Request(messages, requestType, status);
		responses.add(response);
		outputHandler.enqueueRequest(responses);
	}
	
	private void sendRequest(ArrayList<String> messages, double amt, RequestType requestType, Status status) {
		List<Request> responses = new ArrayList<>();
		Request response = new Request(messages, amt, requestType, status);
		responses.add(response);
		outputHandler.enqueueRequest(responses);
	}
	
	private void sendRequest(double amt, RequestType requestType, Status status) {
		List<Request> responses = new ArrayList<>();
		Request response = new Request(amt, requestType, status);
		responses.add(response);
		outputHandler.enqueueRequest(responses);
	}
}
