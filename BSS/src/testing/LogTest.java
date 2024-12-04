package testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import log.*;

class LogTest {
    private static final String LOG_FILE_PATH = "test_logs.txt";
    private File logFile;

    @BeforeEach
    void setUp() throws IOException {
        logFile = new File(LOG_FILE_PATH);
        if (logFile.exists()) {
            logFile.delete();
        }
        logFile.createNewFile();
    }

    @Test
    void testSessionLog() throws IOException {
        SessionLog sessionLog = new SessionLog(1, "Session started", LocalDateTime.now(), LocalDateTime.now(), 1);
        sessionLog.writeLogToFile(logFile);
        String content = new String(Files.readAllBytes(Paths.get(LOG_FILE_PATH)));
        assertTrue(content.contains("Session | Log ID: 1 | Account ID: 1"));
    }

    @Test
    void testDepositLog() throws IOException {
        DepositLog depositLog = new DepositLog(2, "Deposit", 2);
        depositLog.writeLogToFile(logFile);
        String content = new String(Files.readAllBytes(Paths.get(LOG_FILE_PATH)));
        assertTrue(content.contains("Deposit | Log ID: 2 | Account ID: 2"));
    }

    @Test
    void testWithdrawLog() throws IOException {
        WithdrawLog withdrawLog = new WithdrawLog(3, "Withdraw", 3);
        withdrawLog.writeLogToFile(logFile);
        String content = new String(Files.readAllBytes(Paths.get(LOG_FILE_PATH)));
        assertTrue(content.contains("Withdraw | Log ID: 3 | Account ID: 3"));
    }

    @Test
    void testTransferLog() throws IOException {
        TransferLog transferLog = new TransferLog(4, "Transfer", 4, 5);
        transferLog.writeLogToFile(logFile);
        String content = new String(Files.readAllBytes(Paths.get(LOG_FILE_PATH)));
        assertTrue(content.contains("Transfer | Log ID: 4 | From Account ID: 4 | To Account ID: 5"));
    }
}
