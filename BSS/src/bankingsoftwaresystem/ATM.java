package bankingsoftwaresystem;
public class ATM {
	private Session session;
    private double balance;

    public ATM() {
    	session = new Session();
    	this.balance = 0.0;
    }
    
    // Log in with account ID and pin, returning a session
    public Session logIn(int account_id, String in_pin) {
    	
    	//pass arguments account_id and in_pin to session.validate()
    	// this.session.validate(account_id, in_pin);
    	
    	//validate passes args to our sessions' account.checkCredentials() 
    	
    	// gets blurry here, i think it goes:
    	//account.checkCredentials() passes args to Customer.getAccount() 
    	//		plus it passes in that customer's index in the account's customer ArrayList
    	//Customer.getAccount() calls
    	
    	//after all of that, this.session.validate() will return true if we can log in w/ those creds
    	if (this.session.validate(account_id, in_pin)) {
    		//if true then start session with that account
    		//...is this circular?:
    		this.session.startSession(this.session.getAccount());
    	}
    	return session;
    }

    // Log out of the current session
    public void logOut() {
        // Stub: End the session
        session = null;
    }

    // Withdraw a specified amount from the account
    public void withdraw(double amount) {
        // Stub: Replace with withdrawal logic
    }

    // Deposit a specified amount into the account
    public void deposit(double amount) {
        // Stub: Replace with deposit logic
    }

    // Get the current balance of the account
    public double getBal() {
        // Stub: Return the current balance
        return balance;
    }

    // Transfer an amount to another account by account ID
    public void transfer(int toAccount_id, double amount) {
        // Stub: Replace with transfer logic
    }

    // Print a receipt for a given session
    public void printReceipt(Session session) {
        // Stub: Replace with receipt printing logic
    }
}
