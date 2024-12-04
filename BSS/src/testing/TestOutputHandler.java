package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import network.OutputHandler;
import network.Request;

public class TestOutputHandler {

    private ByteArrayOutputStream byteArrayOutputStream;
    private ObjectOutputStream objectOutputStream;
    private OutputHandler outputHandler;

    @BeforeEach
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputHandler = new OutputHandler(objectOutputStream);
    }

    @Test
    public void testEnqueueRequest() throws IOException, InterruptedException {
        List<Request> requests = new ArrayList<>();
        requests.add(new Request());
        requests.add(new Request());

        outputHandler.enqueueRequest(requests);
        Thread outputHandlerThread = new Thread(outputHandler);
        
        outputHandlerThread.start();

        Thread.sleep(500); 

        byte[] outputBytes = byteArrayOutputStream.toByteArray();
        assertNotNull(outputBytes);

        outputHandler.stop();
        outputHandlerThread.join(); 
    }

    @Test
    public void testStop() throws InterruptedException {
    	Thread handlerThread = new Thread(outputHandler);
        handlerThread.start();

        Thread.sleep(200);

        outputHandler.stop();
        handlerThread.join();
        assertFalse(handlerThread.isAlive());
    }
}