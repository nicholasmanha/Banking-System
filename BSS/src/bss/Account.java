package bss;
import java.util.ArrayList;

import enums.AccountType;

public class Account {
	//not sure about static
	static private int count = 0;
	private int account_ID;
	private String pin;
	private ArrayList<Customer> users;
	private boolean frozen;
	private double amount;
	private AccountType AccountType;
	
	Account() {
		this.account_ID = count++;
		this.pin = "";
		this.users = new ArrayList<Customer>();
		this.frozen = false;
		this.amount = 0.0;
		this.AccountType = AccountType.Undefined;
	}
	
	// Check credentials with account ID and pin
    public boolean checkCredentials(int account_ID, String in_pin) {
    	//for each user in this account
    	for (int i = 0; i < users.size(); i++) {
    		Customer customer = users.get(i);
    		 	//check if the log in credentials match an associated user
    			if (this.account_ID == account_ID && this.pin.equals(pin)) {
    				//return true if given user credentials match those of users w/ access to account
    				return true;  
    	        }
    	}
        //if this account's pin == pin then provided pin is valid for account
    	if (this.pin.equals(in_pin)) {
        	return true;
        }
        return false;
    }
    // Get the account ID
    public int getAccountID() {
        return account_ID;
    }
  //pass in account id read in from .txt file
    public void matchUpAccountID(int in_id) {
    	//do nothing if count == id, 
    	if (count == in_id) {
    		return;
    	}
    	// if after new Account(), count is not equal to in_id, increment until it is.
    	// this is to consider the deleted accounts from a previous session as we make new accounts and assign IDs w/ static
    	// count as we read them in from our .txt file 
    	//w/o this, accounts "desync" their assigned IDs from previous session if less accounts exist now then in previous session
    	while(count != in_id) {
    		count++;
    	}
    	this.account_ID = count;
    	return;
    }
    public 
    // Get list of users associated with this account
    public ArrayList<Customer> getUsers() {
    	return users;
    }
    // Add a user to this account
    public void addUser(Customer in_user) {
    	users.add(in_user);
    }
    // Set a new pin for the account
    public void setPin(String in_pin) {
        this.pin = in_pin;
    }
    // Get the current pin
    public String getPin() {
    	return this.pin;
    }
    // Freeze or unfreeze the account
    public void setFrozen(boolean in_freeze) {
        this.frozen = in_freeze;
    }
    // Get the current amount in the account
    public double getAmount() {
    	return this.amount;
    }
    // Set ammount of money in account
    public void setAmount(double in_amount) {
    	this.amount = in_amount;
    }
    // Withdraw an amount from the account
    public void withdraw(double in_amt) {
    	this.amount = this.amount - in_amt;
    }
    // Deposit an amount into the account
    public void deposit(double in_amt) {
        this.amount = this.amount + in_amt;
    }
    //retrieve AccountType
    public AccountType getAccountType() {
    	return this.AccountType;
    }
    //setters for AccountType - used in file reading
    public void setAccountTypeUndefined() {
    	this.AccountType = AccountType.Undefined;
    }
    public void setAccountTypeCheckings() {
    	this.AccountType = AccountType.Checkings;
    }
    public void setAccountTypeSavings() {
    	this.AccountType = AccountType.Savings;
    }
    
}
