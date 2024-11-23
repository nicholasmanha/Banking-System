package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OutputHandler implements Runnable {
	private final ObjectOutputStream outputStream;
	private final ConcurrentLinkedQueue<List<Request>> requestQueue;
	private boolean running = true;

	public OutputHandler(ObjectOutputStream out) {
		this.outputStream = out;
		this.requestQueue = new ConcurrentLinkedQueue<>();
	}

	public void enqueueRequest(List<Request> requests) {
		requestQueue.add(requests);
	}

	public void run() {
		// send responses to client every 200ms
		while (running) {
			List<Request> requests = requestQueue.poll();
			if (requests != null) {
				try {

					outputStream.writeObject(requests);
					outputStream.flush();
					System.out.println("sent message");
				} catch (IOException e) {
					this.stop();
				}
			}

			try {

				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	public void stop() {
		running = false;
	}
}