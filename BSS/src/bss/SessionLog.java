package bss;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class SessionLog implements Log {
   
	private int id;
    private String message;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private int accountID;

    public SessionLog(int id, String message, 
    		LocalDateTime timeStart, LocalDateTime timeEnd, int accountID) {
        
    	this.id = id;
        this.message = message;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.accountID = accountID;
    }
    
    @Override
    public void writeLogToFile(File inOutFile) 
    {
    	try (FileWriter writer = new FileWriter(inOutFile, true)) 
    	{
            writer.write("Log ID: " + id + "\n");
            writer.write("Message: " + message + "\n");
            writer.write("Session Start: " + timeStart.toString() + "\n");
            writer.write("Session End: " + timeEnd.toString() + "\n");
            writer.write("Account ID: " + accountID + "\n");
            writer.write("----\n");
        } 
    	catch (IOException e) 
    	{
            e.printStackTrace();
        }
    }
}
