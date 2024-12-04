package testing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import network.InputHandler;
import network.Request;

import static org.junit.jupiter.api.Assertions.*;

public class TestInputHandler {

    private InputHandler inputHandler;
    private ByteArrayInputStream byteInputStream;

    @BeforeEach
    public void setUp() throws Exception {
    	byteInputStream = createObjectInputStream(new ArrayList<>());
        inputHandler = new InputHandler(new ObjectInputStream(byteInputStream));
    }

    @Test
    public void testgetNextRequest() throws Exception {
        List<Request> requests = Arrays.asList(new Request(), new Request());
        byteInputStream = createObjectInputStream(requests);

        inputHandler = new InputHandler(new ObjectInputStream(byteInputStream));

        Thread handlerThread = new Thread(inputHandler);
        handlerThread.start();

        Thread.sleep(500);

        List<Request> nextRequest = inputHandler.getNextRequest();
        assertEquals(2, nextRequest.size());
        assertNotNull(nextRequest);
        
        inputHandler.stop();
        handlerThread.join();
    }

    @Test
    public void testStop() throws InterruptedException {
        Thread handlerThread = new Thread(inputHandler);
        handlerThread.start();

        Thread.sleep(200);

        inputHandler.stop();
        handlerThread.join();
        assertFalse(handlerThread.isAlive());
    }

    @Test
    public void testGetNextRequestWithNoRequests() {
        assertNull(inputHandler.getNextRequest());
    }

    private ByteArrayInputStream createObjectInputStream(List<Request> requests) throws IOException {
    	ByteArrayInputStream in = null;
        try{
        	ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        	ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(requests);
            in = new ByteArrayInputStream(byteOutputStream.toByteArray());
            return in;
        }
        catch(Exception e){
        	e.getStackTrace();
        }
        return in;
        
    }
}
