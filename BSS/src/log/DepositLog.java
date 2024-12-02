package log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class DepositLog implements Log {
	
	private int id;
    private String message;
    private LocalDateTime timeStamp;
    private int accountID;

    public DepositLog(int id, String message, int accountID) {
        
    	this.id = id;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
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
        return String.format("%s : Deposit | Log ID: %d | Account ID: %d | %s%n",
                timeStamp.toString(), id, accountID, message);
    }

    @Override
    public LocalDateTime getTimeStamp() 
    {
        return timeStamp;
    }
    
}
