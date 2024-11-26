package bss;

import java.util.ArrayList;

import enums.AccountType;

public class Account {
	// not sure about static
	static private int count = 0;
	private int account_ID;
	private String pin;
	//these Customers can access this account, but do not own this account
	private ArrayList<Integer> userIDs;
	private boolean frozen;
	private double amount;
	private AccountType AccountType;

	public Account() {
		this.account_ID = count++;
		this.pin = "";
		this.userIDs = new ArrayList<Integer>();
		this.frozen = false;
		this.amount = 0.0;
		this.AccountType = AccountType.Undefined;
	}
	
	public Account(int in_id) {
		this.account_ID = in_id;
		count = in_id;
		count++;
		this.pin = "";
		this.userIDs = new ArrayList<Integer>();
		this.frozen = false;
		this.amount = 0.0;
		this.AccountType = AccountType.Undefined;
	}
	
	// Check the credentials the customer gives to verify access to this account
    public boolean checkCredentials(int in_account_ID, String in_pin) {
    	//do the credentials match this account? ok check if the user is authorized
    	if (this.account_ID == in_account_ID && this.pin.equals(in_pin)) {
    		// check if customer is associated with the account, removed for now, add back if necessary/possible
//    		if (userIDs.contains(userID)) { 
//    			return true;
//    		}
    		return true;
    	}
    	//otherwise:
        return false;
    }
    // Get the account ID
    public int getAccountID() {
        return account_ID;
    }
    // Get frozen status
    public boolean getOccupied() {
    	return this.frozen;
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
    // Get list of users associated with this account
    public ArrayList<Integer> getUsers() {
    	return this.userIDs;
    }
    // Print users
	public String printUsers() {
		String s = "";
		String Customer_ID = "Customer_ID";
		for (int i = 0; i < userIDs.size(); i++) {
			s = s + Customer_ID + " " + userIDs.get(i);
			if (i != userIDs.size() - 1) {
				s = s + ",";
			}
		}
		return s;
	}
    // Add a user to this account
    public void addUser(int in_userID) {
    	userIDs.add(in_userID);
    }
    // Get the current pin
    public String getPin() {
    	return this.pin;
    }
    // Set a new pin for the account
    public void setPin(String in_pin) {
        this.pin = in_pin;
    }
    // get frozen status of account
    public boolean getFrozen() {
    	return this.frozen;
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
