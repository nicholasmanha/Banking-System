package bss;

import java.util.*;

/**
 * This class is an implementation of DVDUserInterface that uses the console to
 * display the menu of command choices
 */

public class BSSConsoleUI implements BSSUserInterface {

	private Scanner scan;
	private Client client;

	public BSSConsoleUI(Client client) {
		scan = new Scanner(System.in);
		this.client = client;
	}

	public void processCommands() {
		System.out.println("Enter username");
		String username = scan.nextLine();

		System.out.println("Enter password");
		String password = scan.nextLine();

		client.createLoginRequest(username, password);
		
//		System.out.print("Logging in");
//		System.out.println(client.getLoggedIn());
//		while(!client.getLoggedIn()) {
//			try {
//                Thread.sleep(500);  // Pauses for 1 second
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();  // Restore interrupted status
//                System.out.println("Thread was interrupted.");
//            }
//            System.out.print(".");
//		}
		
		String[] commands = { "Deposit", };

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
					doDeposit();
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

	private void doDeposit() {
		System.out.println("Enter amount");
		Double amount = scan.nextDouble();
		client.createDepositRequest(amount);
	}

}
