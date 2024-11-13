package bankingsoftwaresystem;
import java.util.ArrayList;

public class Session {
	private Account account;
	//private DateTime start_time;
	//private DateTime end_time;
	private ArrayList<Log> logs;
	
	public Session() {
		this.account = new Account();
		//this start_time = new DateTime();
		//this end_time = new DateTime();
		this.logs = new ArrayList<Log>();
	}
	// Validate credentials with account ID and pin
    public boolean validate(int account_id, String in_pin) {
    	//this will return a bool, AND set the attributes of this session's account to the account info from DB
    	//so we can start a session using it
    	if (this.account.checkCredentials(account_id, in_pin)) {
    		//if checkCredentials is true, then this.account should hold the proper acc info
    		return true;
    	}
    	return false;
    }
    // Start a session with a given account
    public boolean startSession(Account account) {
        
    	//if (validate(account.getAccountID(), account.getPin())) {
    		//return true;
    	//}
        //return false;
    	
    	return true; //this passes true to processCommands(); (?)
    }
    // End the current session
    public void endSession() {
        this.account = null;
    }
    // Get the current account in the session
    public Account getAccount() {
        return this.account;
    }
    // Get a list of all logs
    public ArrayList<Log> getLogs() {
        return this.logs;
    }
    // Add a log entry to the logs
    private void addLog(Log log) {
        this.logs.add(log);
    }

	
	
}
