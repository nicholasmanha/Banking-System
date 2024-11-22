package bss;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import requests.Request;

public class InputHandler implements Runnable {
	private final ObjectInputStream inputStream;
	private final ConcurrentLinkedQueue<List<Request>> requestQueue;
	private boolean running = true;

	public InputHandler(ObjectInputStream in) {
		this.inputStream = in;
		this.requestQueue = new ConcurrentLinkedQueue<>();
	}

	public void run() {
		// put requests in queue for processing (which is done in the ClientHandler.run())
		while (running) {
			try {
				List<Request> requests = (List<Request>) inputStream.readObject();
				if (requests != null) {
					requestQueue.add(requests);
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				running = false;
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

	public List<Request> getNextRequest() {
		return requestQueue.poll();
	}
}