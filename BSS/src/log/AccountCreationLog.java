package log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AccountCreationLog implements Log {
	
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
    
    @Override
    public void writeLogToFile(File inOutFile) 
    {
    	try (FileWriter writer = new FileWriter(inOutFile, true)) 
    	{
            writer.write(toLogString());
        } 
    	catch (IOException e) 
    	{
            e.printStackTrace();
        }
    }
    
    @Override
    public String toLogString() 
    {
        return String.format("%s : Account Creation | ID: %d | Teller ID: %d | Account ID: %d | %s%n",
                timeStamp.toString(), id, teller_ID, accountID, message);
    }

    @Override
    public LocalDateTime getTimeStamp() 
    {
        return timeStamp;
    }

}
