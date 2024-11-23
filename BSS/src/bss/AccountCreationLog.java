package bss;

import java.time.LocalDateTime;

public class AccountCreationLog {
	
	private int id;
	private int teller_ID;
    private String message;
    private LocalDateTime timeStamp;
    private int accountID;
    
    public AccountCreationLog(int id, int teller_ID, String message, 
    		LocalDateTime timeStamp, int accountID) {
    	
        this.id = id;
        this.teller_ID = teller_ID;
        this.message = message;
        this.timeStamp = timeStamp;
        this.accountID = accountID;
    }

}
