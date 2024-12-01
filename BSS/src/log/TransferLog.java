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
    
    public TransferLog(int id, String message, LocalDateTime timeStamp, 
    		int accountID, int toAccountID) {
    	
        this.id = id;
        this.message = message;
        this.timeStamp = timeStamp;
        this.accountID = accountID;
        this.toAccountID = toAccountID;
    }
    
    @Override
    public void writeLogToFile(File inOutFile) 
    {
        try (FileWriter writer = new FileWriter(inOutFile, true)) 
        {
            writer.write("Log ID: " + id + "\n");
            writer.write("Message: " + message + "\n");
            writer.write("Timestamp: " + timeStamp.toString() + "\n");
            writer.write("From Account ID: " + accountID + "\n");
            writer.write("To Account ID: " + toAccountID + "\n");
            writer.write("----\n");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
