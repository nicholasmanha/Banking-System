package log;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;
import java.time.LocalDateTime;

public interface Log {
	public void writeLogToFile(File inOutFile);
	public String toLogString();
	public LocalDateTime getTimeStamp();
	

}
