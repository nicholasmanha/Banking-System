package log;
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
        return String.format("%s - %s : Session | Log ID: %d | Account ID: %d | %s%n",
                timeStart.toString(), timeEnd.toString(), id, accountID, message);
    }

    @Override
    public LocalDateTime getTimeStamp() 
    {
        return timeStart;
    }
}
