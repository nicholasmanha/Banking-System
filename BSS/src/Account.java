import java.util.ArrayList;

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
    				//if they do, get the account data from bank
    	            Account temp  = new Account();
    	            temp = customer.getAccount(account_ID, in_pin, i);
    	            //set values of this account to values from bank(?)
    	            this.amount = temp.getAmount();
    	            this.AccountType = temp.AccountType;
    	            this.account_ID = temp.account_ID;
    	            this.pin = temp.getPin();
    	            this.users = temp.getUsers();
    	            //i feel like this is wrong, might result in account_IDs incrementing by 2s
    	        	return true;  // Return the matching customer
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
    // Withdraw an amount from the account
    public boolean withdraw(double in_amt) {
    	this.amount = this.amount - in_amt;
    	return true;
    }
    // Deposit an amount into the account
    public boolean deposit(double in_amt) {
        this.amount = this.amount + in_amt;
    	return true;
    }
}
