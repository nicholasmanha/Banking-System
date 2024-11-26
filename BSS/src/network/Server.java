package network;

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

//			customer.addAccount(testAccount);
//			testAccount.addUser(0);
			
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
		 * Conditionals for processing incoming requests
		 */
		private static void processRequest(List<Request> req) {

			// for every request in the list of requests that was received
			for (Request request : req) {
				RequestType type = request.getType();
				Status status = request.getStatus();

				if (type == RequestType.LOGIN && request.getStatus() == Status.REQUEST) {
					doLogin(request);
				}
				if (type == RequestType.LOGOUT && request.getStatus() == Status.REQUEST) {
					doLogout(request);
				}
				if (type == RequestType.DEPOSIT) {
					doDeposit(request);
				}
				if (type == RequestType.WITHDRAW) {
					doWithdraw(request);
				}
				if (type == RequestType.TRANSFER) {
					doTransfer(request);
				}
			}
		}

		private static void doLogin(Request request) {
			System.out.println("Login Request Received");
			String username_string = request.getTexts().get(0);
			int username = Integer.parseInt(username_string);
			String password = request.getTexts().get(1);
			System.out.println(username + ", " + password);

			userType = determineUserType(bank, username);
			System.out.println(userType + "");
			if (userType == UserType.CUSTOMER) {
				Account acc = bank.findAccount(username);
				if (acc.checkCredentials(username, password)) {

					// send login success
					List<Request> loginResponses = new ArrayList<>();
					Request loginResponse = new Request(UserType.CUSTOMER, RequestType.LOGIN, Status.SUCCESS);
					loginResponses.add(loginResponse);

					outputHandler.enqueueRequest(loginResponses);
					loggedIn = true;

					// initialized global session variable
					session = atm.logIn(acc);
				} else {
					// user credentials were incorrect, send failure response
					List<Request> loginResponses = new ArrayList<>();
					Request loginResponse = new Request(UserType.CUSTOMER, RequestType.LOGIN, Status.FAILURE);
					loginResponses.add(loginResponse);

					outputHandler.enqueueRequest(loginResponses);
				}
			} else if (userType == UserType.TELLER) {
				Teller teller = bank.findTeller(username);
				if(teller.checkCredentials(username, password)) {
					List<Request> loginResponses = new ArrayList<>();
					Request loginResponse = new Request(UserType.TELLER, RequestType.LOGIN, Status.SUCCESS);
					loginResponses.add(loginResponse);

					outputHandler.enqueueRequest(loginResponses);
					loggedIn = true;
				}
				
			}
			// user isn't a teller or a customer, send failure response
			else {
				List<Request> loginResponses = new ArrayList<>();
				Request loginResponse = new Request(UserType.CUSTOMER, RequestType.LOGIN, Status.FAILURE);
				loginResponses.add(loginResponse);

				outputHandler.enqueueRequest(loginResponses);
			}
		}

		private static void doDeposit(Request request) {
			if (loggedIn == true) {
				// getOccupied is for checking if the account is currently processing something
				// so two people can't interact with an account at once
				if (session.getAccount().getOccupied() == false) {
					// set the frozen flag to true on account whilst interacting with it
					session.getAccount().setFrozen(true);
					session.getAccount().deposit(request.getAmount());
					session.getAccount().setFrozen(false);

					// make the deposit response
					List<Request> depositResponses = new ArrayList<>();
					Request depositResponse = new Request(RequestType.DEPOSIT, Status.SUCCESS);
					depositResponses.add(depositResponse);

					// enqueue the response so it can be delivered
					outputHandler.enqueueRequest(depositResponses);

					// debug
					System.out.println("New Balance: " + session.getAccount().getAmount());
				} else {
					// send deposit failure response if the account is occupied
					List<Request> accountOccupiedResponses = new ArrayList<>();
					ArrayList<String> errorMessage = new ArrayList<String>();
					errorMessage.add("Account Occupied\n");
					Request accountOccupiedResponse = new Request(errorMessage, RequestType.DEPOSIT, Status.FAILURE);
					accountOccupiedResponses.add(accountOccupiedResponse);

					outputHandler.enqueueRequest(accountOccupiedResponses);
				}

			}
		}

		private static void doWithdraw(Request request) {
			if (loggedIn == true) {
				// if they have insufficient funds
				if (session.getAccount().getAmount() < request.getAmount()) {
					List<Request> insufficientFundsResponses = new ArrayList<>();
					ArrayList<String> errorMessage = new ArrayList<String>();
					errorMessage.add("Insufficient Funds");
					Request insufficientFundsResponse = new Request(errorMessage, RequestType.WITHDRAW, Status.FAILURE);
					insufficientFundsResponses.add(insufficientFundsResponse);

					outputHandler.enqueueRequest(insufficientFundsResponses);
				} else {
					// Account has sufficient funds, withdraw and send success

					session.getAccount().withdraw(request.getAmount());
					List<Request> withdrawResponses = new ArrayList<>();
					Request withdrawResponse = new Request(RequestType.WITHDRAW, Status.SUCCESS);
					withdrawResponses.add(withdrawResponse);

					outputHandler.enqueueRequest(withdrawResponses);
					System.out.println("New Balance: " + session.getAccount().getAmount());
				}

			}
		}

		private static void doTransfer(Request request) {
			if (loggedIn == true) {
				// Account has insufficient funds, send failure
				if (session.getAccount().getAmount() < request.getAmount()) {
					List<Request> insufficientFundsResponses = new ArrayList<>();
					ArrayList<String> errorMessage = new ArrayList<String>();
					errorMessage.add("Insufficient Funds\n");
					Request insufficientFundsResponse = new Request(errorMessage, RequestType.TRANSFER, Status.FAILURE);
					insufficientFundsResponses.add(insufficientFundsResponse);

					outputHandler.enqueueRequest(insufficientFundsResponses);
				} else {
					// to_account wasn't found, send failure
					session.getAccount().withdraw(request.getAmount());
					Account to_account = bank.findAccount(Integer.parseInt(request.getTexts().get(0)));
					if (to_account == null) {
						List<Request> accountNotFoundResponses = new ArrayList<>();
						ArrayList<String> errorMessage = new ArrayList<String>();
						errorMessage.add("Account Not Found\n");
						Request accountNotFoundResponse = new Request(errorMessage, RequestType.TRANSFER,
								Status.FAILURE);
						accountNotFoundResponses.add(accountNotFoundResponse);

						outputHandler.enqueueRequest(accountNotFoundResponses);
					} else {
						// Account has sufficient funds, transfer and send success
						to_account.deposit(request.getAmount());
						List<Request> transferResponses = new ArrayList<>();
						Request transferResponse = new Request(RequestType.TRANSFER, Status.SUCCESS);
						transferResponses.add(transferResponse);

						outputHandler.enqueueRequest(transferResponses);
						System.out.println("New Balance: " + session.getAccount().getAmount());
					}

				}
			}
		}

		private static void doLogout(Request request) {
			if (loggedIn) {
				atm.logOut();
				List<Request> logoutResponses = new ArrayList<>();
				Request logoutResponse = new Request(RequestType.LOGOUT, Status.SUCCESS);
				logoutResponses.add(logoutResponse);

				outputHandler.enqueueRequest(logoutResponses);
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
