package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import bss.ATM;
import bss.Account;
import bss.Bank;
import bss.Customer;
import bss.Session;
import bss.Teller;
import enums.*;

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
		private static OutputHandler outputHandler;
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

			/*
			 * creating accounts for debugging purposes
			 */
			Teller firstTeller = new Teller("password");
			bank.addTeller(firstTeller);

			Customer customer = new Customer();
			bank.addCustomer(customer);

			Account testAccount = firstTeller.createAccount("123");
			bank.addAccount(testAccount);

			Account testAccount2 = firstTeller.createAccount("321");
			bank.addAccount(testAccount2);

			for (Account account : bank.getAccounts()) {
				System.out.println("account #" + account.getId());
			}

			for (Teller teller : bank.getTellers()) {
				System.out.println("teller #" + teller.getId());
			}
			/*
			 * Establish input and output streams and inputHandler and outputHandler,
			 * initialize threads for them and process requests in a regular interval
			 */
			try {
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

				InputHandler inputHandler = new InputHandler(objectInputStream);
				Thread inputThread = new Thread(inputHandler);
				inputThread.start();

				OutputHandler outHandler = new OutputHandler(objectOutputStream);
				ClientHandler.outputHandler = outHandler;
				Thread outputThread = new Thread(outHandler);
				outputThread.start();

				// process requests every 200ms
				while (true) {
					List<Request> req = inputHandler.getNextRequest();
					if (req != null) {
						processRequest(req);
					}
					Thread.sleep(200);
				}

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		/*
		 * Switch statement for processing incoming requests
		 */
		private static void processRequest(List<Request> req) {

			// for every request in the list of requests that was received
			for (Request request : req) {
				RequestType type = request.getType();
				switch (type) {
				case LOGIN:
					doLogin(request);
					break;
				case LOGOUT:
					doLogout(request);
					break;
				case DEPOSIT:
					doDeposit(request);
					break;
				case WITHDRAW:
					doWithdraw(request);
					break;
				case TRANSFER:
					doTransfer(request);
					break;
				case FREEZE:
					doFreeze(request);
					break;
				default:
					break;
				}

			}
		}

		private static void sendResponse(UserType userType, RequestType requestType, Status status) {
			List<Request> responses = new ArrayList<>();
			Request response = new Request(userType, requestType, status);
			responses.add(response);
			outputHandler.enqueueRequest(responses);
		}

		private static void sendResponse(RequestType requestType, Status status) {
			List<Request> responses = new ArrayList<>();
			Request response = new Request(requestType, status);
			responses.add(response);
			outputHandler.enqueueRequest(responses);
		}

		private static void sendResponse(ArrayList<String> messages, RequestType requestType, Status status) {
			List<Request> responses = new ArrayList<>();
			Request response = new Request(messages, requestType, status);
			responses.add(response);
			outputHandler.enqueueRequest(responses);
		}

		private static void doLogin(Request request) {
			int username = Integer.parseInt(request.getTexts().get(0));
			String password = request.getTexts().get(1);

			userType = determineUserType(bank, username);

			if (userType == UserType.CUSTOMER) {
				Account acc = bank.findAccount(username);
				if (acc.checkCredentials(username, password)) {
					sendResponse(UserType.CUSTOMER, RequestType.LOGIN, Status.SUCCESS);
					loggedIn = true;

					// initialized global session variable
					session = atm.logIn(acc);
				} else {
					// user credentials were incorrect, send failure response
					sendResponse(UserType.CUSTOMER, RequestType.LOGIN, Status.FAILURE);
				}
			} else if (userType == UserType.TELLER) {
				Teller teller = bank.findTeller(username);
				if (teller.checkCredentials(username, password)) {
					sendResponse(UserType.TELLER, RequestType.LOGIN, Status.SUCCESS);
					loggedIn = true;
				}
			}
			// user isn't a teller or a customer, send failure response
			else {
				sendResponse(UserType.CUSTOMER, RequestType.LOGIN, Status.FAILURE);
			}
		}

		private static void doDeposit(Request request) {
			if (loggedIn == true) {
				// getOccupied is for checking if the account is currently processing something
				// so two people can't interact with an account at once
				if (session.getAccount().getOccupied() == false && session.getAccount().getFrozen() == false) {
					// set the frozen flag to true on account whilst interacting with it
					session.getAccount().setFrozen(true);
					session.getAccount().deposit(request.getAmount());
					session.getAccount().setFrozen(false);

					sendResponse(RequestType.DEPOSIT, Status.SUCCESS);
				} else {
					// send deposit failure response if the account is occupied
					ArrayList<String> errorMessage = new ArrayList<String>(
							Arrays.asList("Account Occupied or Frozen\n"));
					sendResponse(errorMessage, RequestType.DEPOSIT, Status.FAILURE);
				}
			}
		}

		private static void doWithdraw(Request request) {
			if (loggedIn == true) {
				if (session.getAccount().getOccupied() == false && session.getAccount().getFrozen() == false) {
					// if they have insufficient funds
					if (session.getAccount().getAmount() < request.getAmount()) {
						ArrayList<String> errorMessage = new ArrayList<String>(Arrays.asList("Insufficient Funds"));
						sendResponse(errorMessage, RequestType.WITHDRAW, Status.FAILURE);
					} else {
						// Account has sufficient funds, withdraw and send success
						sendResponse(RequestType.WITHDRAW, Status.SUCCESS);
					}
				} else {
					// send deposit failure response if the account is occupied
					ArrayList<String> errorMessage = new ArrayList<String>(
							Arrays.asList("Account Occupied or Frozen\n"));
					sendResponse(errorMessage, RequestType.WITHDRAW, Status.FAILURE);
				}
			}
		}

		private static void doTransfer(Request request) {
			if (loggedIn == true) {
				if (session.getAccount().getOccupied() == false && session.getAccount().getFrozen() == false) {
					// Account has insufficient funds, send failure
					if (session.getAccount().getAmount() < request.getAmount()) {
						ArrayList<String> errorMessage = new ArrayList<String>(Arrays.asList("Insufficient Funds\n"));
						sendResponse(errorMessage, RequestType.TRANSFER, Status.FAILURE);
					} else {
						// to_account wasn't found, send failure
						session.getAccount().withdraw(request.getAmount());
						Account to_account = bank.findAccount(Integer.parseInt(request.getTexts().get(0)));
						if (to_account == null) {
							ArrayList<String> errorMessage = new ArrayList<String>(
									Arrays.asList("Account Not Found\n"));
							sendResponse(errorMessage, RequestType.TRANSFER, Status.FAILURE);
						} else {
							// Account has sufficient funds, transfer and send success
							to_account.deposit(request.getAmount());
							sendResponse(RequestType.TRANSFER, Status.SUCCESS);
						}
					}
				} else {
					// send deposit failure response if the account is occupied
					ArrayList<String> errorMessage = new ArrayList<String>(
							Arrays.asList("Account Occupied or Frozen\n"));
					sendResponse(errorMessage, RequestType.TRANSFER, Status.FAILURE);
				}
			}
		}

		private static void doFreeze(Request request) {
			if (loggedIn) {
				if (userType == UserType.TELLER) {
					int acc_ID = Integer.parseInt(request.getTexts().get(0));
					Account account = bank.findAccount(acc_ID);
					account.setFrozen(true);
					sendResponse(RequestType.FREEZE, Status.SUCCESS);
				}
			}
		}

		private static void doLogout(Request request) {
			if (loggedIn) {
				atm.logOut();
				sendResponse(RequestType.LOGOUT, Status.SUCCESS);
			}
		}

		// based on the given username, determine if it is for an account or a teller
		private static UserType determineUserType(Bank bank, int userID) {
			Teller teller;
			Account acc;
			acc = bank.findAccount(userID);
			if (acc == null) {
				teller = bank.findTeller(userID);
				if (teller == null) {
					System.out.println("Account Undefined\n");
					return UserType.UNDEFINED;
				}
				return UserType.TELLER;
			}
			return UserType.CUSTOMER;
		}
	}
}