package bss;
import java.util.ArrayList;

import log.Log;

import java.time.LocalDateTime;

public class Session {
	private Account account;
	private LocalDateTime start_time;
	private LocalDateTime end_time;
	private ArrayList<Log> logs;
	
	public Session() {
		
		this.setStart_time(LocalDateTime.now());
		this.setEnd_time(LocalDateTime.now());
		this.logs = new ArrayList<Log>();
	}

    // Start a session with a given account
    public boolean startSession(Account in_account) {
    	this.account = in_account;
    	// note start time of the session
    	this.setStart_time(LocalDateTime.now());
    	//insert code for interacting with server
    	
    	return true; 
    }
    // End the current session
    public void endSession() {
    	//when we end the session, the time is recorded
    	this.setEnd_time(LocalDateTime.now());
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
    // get the start time of session
	public LocalDateTime getStart_time() {
		return start_time;
	}
    // set start time of session
	public void setStart_time(LocalDateTime start_time) {
		this.start_time = start_time;
	}
	// get end time of session
	public LocalDateTime getEnd_time() {
		return end_time;
	}
	//get set end time of session
	public void setEnd_time(LocalDateTime end_time) {
		this.end_time = end_time;
	}
}
