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
		try (Socket socket = new Socket("192.168.56.1", 1234)) {
			// output, to send TO the server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

			// input, to recieve FROM the server
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			// create a new thread object
			OutHandler outHandler = new OutHandler(objectOutputStream);

			// This thread will handle the client
			// separately
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
						System.out.println("sending requests");
						outputStream.writeObject(requests);
						outputStream.flush();
					} catch (IOException e) {
						System.out.println("request wasn't null but something went wrong");
						running = false;
					}
				}
				System.out.println("request was null");
				try {
					
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

}
