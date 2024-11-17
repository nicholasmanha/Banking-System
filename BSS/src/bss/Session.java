package bss;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Session {
	private Account account;
	private LocalDateTime start_time;
	private LocalDateTime end_time;
	private ArrayList<Log> logs;
	
	public Session() {
		this.account = new Account();
		this.start_time = LocalDateTime.now();
		this.end_time = LocalDateTime.now();
		this.logs = new ArrayList<Log>();
	}

    // Start a session with a given account
    public boolean startSession(Account in_account) {
    	// note start time of the session
    	this.start_time = LocalDateTime.now();
    	//insert code for interacting with server
    	
    	return true; 
    }
    // End the current session
    public void endSession() {
    	//when we end the session, the time is recorded
    	this.end_time = LocalDateTime.now();
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
