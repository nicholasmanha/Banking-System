package log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class TransferLog implements Log {
	
	private int id;
    private String message;
    private LocalDateTime timeStamp;
    private int accountID;
    private int toAccountID;
    
    public TransferLog(int id, String message,
    		int accountID, int toAccountID) {
    	
        this.id = id;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
        this.accountID = accountID;
        this.toAccountID = toAccountID;
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
        return String.format("%s : Transfer | Log ID: %d | From Account ID: %d | To Account ID: %d | %s%n",
                timeStamp.toString(), id, accountID, toAccountID, message);
    }

    @Override
    public LocalDateTime getTimeStamp() 
    {
        return timeStamp;
    }
}
