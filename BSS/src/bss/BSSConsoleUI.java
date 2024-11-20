package bss;

import java.util.*;

/**
 *  This class is an implementation of DVDUserInterface
 *  that uses the console to display the menu of command choices
 */


public class BSSConsoleUI implements BSSUserInterface {
	
	private Scanner scan;
	
	public BSSConsoleUI() {
		scan = new Scanner(System.in);
	}
	
	public void processCommands()
	{
		 String[] commands = {"Deposit",
	 			  "Withdraw",
	 			  "Transfer",
	 			  "Logout"
	 			  };

	}

	private void doLogIn() {

	}
	
}
