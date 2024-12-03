package bss;

import java.util.*;

import enums.UserType;
import network.Client;

public class BSSConsoleUI implements Runnable {

	private Scanner scan;
	private Client client;

	public BSSConsoleUI(Client client) {
		scan = new Scanner(System.in);
		this.client = client;
	}

	public void run() {
		boolean success = false;
		while(!success) {
			// Logging in
			System.out.println("Enter username");
			String username = scan.nextLine();

			System.out.println("Enter password");
			String password = scan.nextLine();

			client.createLoginRequest(username, password);

			System.out.print("Logging in");
			loadingDots();
			
			System.out.println(client.getResponseMessage());

			// determine which view to show based off who is logging in
			if (client.getUserType() == UserType.CUSTOMER) {
				success=true;
				AccountView(true);
				break;

			} else if(client.getUserType() == UserType.TELLER) {
				success=true;
				tellerView();
				
				break;
			}
			else {
				success = false;
			}
		}
		

	}

	/*
	 * Teller methods
	 */
	private void doCreateCustomer() {
		client.createCustomerCreationRequest();
		loadingDots();
	    System.out.println(client.getResponseMessage());
	}

	private void doCreateAccount() {
		System.out.println("Enter Password");
	    String password = scan.nextLine();
	    System.out.println("Enter Customer ID to be Added");
	    String cust_id = scan.nextLine();
	    client.createAccountCreationRequest(password);
	    loadingDots();
	    System.out.println(client.getResponseMessage());
		
	}

	private void doReadLogs() {
		System.out.println("Enter start date-time (yyyy-MM-ddTHH:mm:ss):");
	    String startDate = scan.nextLine();
	    System.out.println("Enter end date-time (yyyy-MM-ddTHH:mm:ss):");
	    String endDate = scan.nextLine();

	    client.createReadLogsRequest(startDate, endDate);
	    System.out.print("Getting Logs");
	    loadingDots();
	    System.out.println(client.getResponseMessage());
	}

	private void doFreeze() {
		System.out.println("Enter account ID:");
		int acc_ID = scan.nextInt();
		client.createFreezeRequest(acc_ID);
		System.out.print("Freezing Account");
		loadingDots();
		System.out.println(client.getResponseMessage());

	}

	private void doEnterAccount() {
		System.out.println("Enter Account ID:");
		int acc_ID = scan.nextInt();
		client.createEnterAccountRequest(acc_ID);
		System.out.print("Accessing Account");
		loadingDots();
		System.out.println(client.getResponseMessage());
		if (client.getAccountAccessed()) {
			AccountView(false);
		}

	}
	
	/*
	 * Account Methods
	 */
	
	private void doDeposit() {

		System.out.println("Enter amount");
		Double amount = scan.nextDouble();
		client.createDepositRequest(amount);
		System.out.print("Depositing");
		loadingDots();
		System.out.println(client.getResponseMessage());
	}

	private void doWithdraw() {
		System.out.println("Enter amount");
		Double amount = scan.nextDouble();
		client.createWithdrawRequest(amount);
		System.out.print("Withdrawing");
		loadingDots();
		System.out.println(client.getResponseMessage());
	}

	private void doTransfer() {
		System.out.println("Enter Account id you wish to send money to");
		int id = scan.nextInt();
		System.out.println("Enter amount");
		Double amount = scan.nextDouble();
		client.createTransferRequest(id, amount);

		System.out.print("Transfering");
		loadingDots();
		System.out.println(client.getResponseMessage());
	}

	private void doLogout() {
		System.out.println("cya");
		client.createLogoutRequest();

	}

	private void doLeave() {
		client.createLeaveRequest();
		System.out.print("Leaving Account");
		loadingDots();
		if (!client.getAccountAccessed()) {
			System.out.println();
			tellerView();
		}
	}

	/*
	 * Views
	 */
	
	private void tellerView() {
		String[] commands = { "Enter Account", "Freeze", "Read Logs", "Create Customer", "Create Account", "Logout" };

		int choice;

		do {
			for (int i = 0; i < commands.length; i++) {
				System.out.println("Select " + i + ": " + commands[i]);
			}
			try {
				choice = scan.nextInt();
				scan.nextLine();
				switch (choice) {
				case 0:
					doEnterAccount();
					break;
				case 1:
					doFreeze();
					break;
				case 2:
					doReadLogs();
					break;
				case 3:
					doCreateCustomer();
					break;
				case 4:
					doCreateAccount();
					break;
				case 5:
					doLogout();
					break;
				default:
					System.out.println("INVALID CHOICE - TRY AGAIN");
				}
			} catch (InputMismatchException e) {
				System.out.println("INVALID CHOICE - TRY AGAIN");
				scan.nextLine();
				choice = -1;
			}
		} while (choice != commands.length - 1);

	}

	private void AccountView(boolean customer) {
		String[] commands;
		
		// if the user is a customer, have the last option be "logout", if they are a teller then this means that they are
		// accessing an account, so the last option is "Leave Account".
		if (customer) {
			commands = new String[] { "Deposit", "Withdraw", "Transfer", "Logout" };
		} else {
			commands = new String[] { "Deposit", "Withdraw", "Transfer", "Leave Account" };
		}

		int choice;

		do {
			System.out.println("Balance: $");
			for (int i = 0; i < commands.length; i++) {
				System.out.println("Select " + i + ": " + commands[i]);
			}
			try {
				choice = scan.nextInt();
				scan.nextLine();
				switch (choice) {
				case 0:
					doDeposit();
					break;
				case 1:
					doWithdraw();
					break;
				case 2:
					doTransfer();
					break;
				case 3:
					if (customer) {
						doLogout();
					} else {
						doLeave();
					}

					break;
				default:
					System.out.println("INVALID CHOICE - TRY AGAIN");
				}
			} catch (InputMismatchException e) {
				System.out.println("INVALID CHOICE - TRY AGAIN");
				scan.nextLine();
				choice = -1;
			}
		} while (choice != commands.length - 1);

	}
	
	// Display "...." for loading responses from server
	private void loadingDots() {
		while (client.getIsProcessing()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println("Thread was interrupted.");
			}
			System.out.print(".");
		}
	}

}
