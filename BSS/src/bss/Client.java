package bss;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import requests.Request;
import enums.*;

public class Client {
	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 1234)) {
			
			// output, to send TO the server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            InputHandler inputHandler = new InputHandler(objectInputStream);
            Thread inputThread = new Thread(inputHandler);
            inputThread.start();
            
            
			OutHandler outHandler = new OutHandler(objectOutputStream);

			Thread outputThread = new Thread(outHandler);
			outputThread.start();
			
			List<Request> reqs = new ArrayList<>();
			reqs.add(createLoginRequest("1", "123"));
			
			List<Object> objectList = new ArrayList<>(reqs);
			outHandler.enqueueRequest(objectList);
			
			outputThread.join();
			System.out.println("Closing socket");
			
			socket.close();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static Request createLoginRequest(String username, String password) {
		ArrayList<String> userAndPass = new ArrayList<String>();
		Request loginRequest = new Request(userAndPass, RequestType.LOGIN, Status.REQUEST);
		return loginRequest;
	}

	private static class InputHandler implements Runnable {
		private final ObjectInputStream inputStream;
		private final ConcurrentLinkedQueue<List<Object>> requestQueue;
		private boolean running = true;

		public InputHandler(ObjectInputStream in) {
			this.inputStream = in;
			this.requestQueue = new ConcurrentLinkedQueue<>();
		}

		public void enqueueRequest(List<Object> requests) {
			requestQueue.add(requests);
		}

		public void run() {
			while (running) {
				try {
					List<Object> requests = (List<Object>) inputStream.readObject();
					if( requests != null) {
						requestQueue.add(requests);
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
					running = false;
				}

				try {

					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
		public void stop() {
	        running = false;
	    }
		public Object getNextMessage() {
	        return requestQueue.poll();
	    }
	}
	
	private static class OutHandler implements Runnable {
		private final ObjectOutputStream outputStream;
		private final ConcurrentLinkedQueue<List<Object>> requestQueue;
		private boolean running = true;

		public OutHandler(ObjectOutputStream out) {
			this.outputStream = out;
			this.requestQueue = new ConcurrentLinkedQueue<>();
		}

		public void enqueueRequest(List<Object> requests) {
			requestQueue.add(requests);
		}

		public void run() {
			while (running) {
				List<Object> requests = requestQueue.poll();
				if (requests != null) {
					try {
						outputStream.writeObject(requests);
						outputStream.flush();
					} catch (IOException e) {
						running = false;
					}
				}
				try {
					
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

}
