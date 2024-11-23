package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputHandler implements Runnable {
	private final ObjectInputStream inputStream;
	private final ConcurrentLinkedQueue<List<Request>> requestQueue;
	private boolean running = true;

	public InputHandler(ObjectInputStream in) {
		this.inputStream = in;
		this.requestQueue = new ConcurrentLinkedQueue<>();
	}

	public void run() {
	    while (running) {
	        try {
	            // Read objects from the input stream
	            List<Request> requests = (List<Request>) inputStream.readObject();
	            if (requests != null) {
	                requestQueue.add(requests);
	            }
	        } catch (IOException e) {
	            if (e instanceof java.io.EOFException) {
	                System.out.println("Connection closed by server.");
	            } else {
	                e.printStackTrace();
	            }
	            running = false; // Stop the thread
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	            this.stop();
	        }

	        try {
	            Thread.sleep(200); // Avoid busy-waiting
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}


	public void stop() {
		running = false;
	}

	public List<Request> getNextRequest() {
		return requestQueue.poll();
	}
}