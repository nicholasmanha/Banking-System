package bss;

import java.util.ArrayList;

public class ATM {
	private Session session;
    private double balance;

    public ATM() {
    	session = new Session();
    	this.balance = 0.0;
    }
    // log in with account, which is filed with the correct info from the Bank DB prior to logIn
    public Session logIn(Account account) {   
    	session.startSession(account);
    	return session;
    }

    // Log out of the current session
    public void logOut() {
        session.endSession();
    }

    // Withdraw a specified amount from the account
    public void withdraw(double amount) {
       this.session.getAccount().withdraw(amount);
    }

    // Deposit a specified amount into the account
    public void deposit(double amount) {
    	this.session.getAccount().deposit(amount);
    }

    // Get the current balance of the account
    public double getBal() {
    	this.balance = this.session.getAccount().getAmount();
        return balance;
    }

    // Transfer an amount to another account, pass in destination account similarly to how we passed
    // in an account to use for the session
    public void transfer(Account account, double txr_amount) {
        //remove txr_amount from this session's account
    	this.session.getAccount().withdraw(txr_amount);
    	//add same amount to destination account
    	account.deposit(txr_amount);
    }

    // Print a receipt for a given session
    public void printReceipt() {
    	//get the array of logs belonging to the session
        ArrayList<Log> logs = this.session.getLogs();
        //print the last log, as this is the last operation done on the session, for a receipt (?)
        System.out.println(logs.get(logs.size() - 1));
    }
}
