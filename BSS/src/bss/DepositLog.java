package bss;

import java.time.LocalDateTime;

public class DepositLog implements Log {
	
	private int id;
    private String message;
    private LocalDateTime timeStamp;
    private int accountID;

    public DepositLog(int id, String message, LocalDateTime timeStamp, int accountID) {
        
    	this.id = id;
        this.message = message;
        this.timeStamp = timeStamp;
        this.accountID = accountID;
    }
    
}
