package network;

import bss.BSSConsoleUI;
import enums.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private boolean alive = true;
    private boolean loggedIn = false;
    private boolean isProcessing = false;
    private String responseMessage;
    private UserType userType;

    public Client(InputHandler inputHandler, OutputHandler outputHandler) {
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PORT = 1234;

        Socket socket = null;
        try {
            // Establishing the socket connection
            socket = new Socket(HOST, PORT);

            // Output stream setup
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Input stream setup
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Handlers
            InputHandler inputHandler = new InputHandler(objectInputStream);
            OutputHandler outputHandler = new OutputHandler(objectOutputStream);

            // Threads for handlers
            Thread inputThread = new Thread(inputHandler);
            Thread outputThread = new Thread(outputHandler);
            inputThread.start();
            outputThread.start();

            // Create client instance
            Client client = new Client(inputHandler, outputHandler);

            // Start GUI
            BSSConsoleUI UI = new BSSConsoleUI(client);
            Thread consoleThread = new Thread(UI);
            consoleThread.start();

            // Process server responses
            while (client.alive) {
                List<Request> req = inputHandler.getNextRequest();
                if (req != null) {
                    client.processResponse(req);
                }
                Thread.sleep(200);
            }

            System.out.println("Closing socket...");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Ensuring the socket is closed, even if an exception occurred
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle error closing the socket
            }
        }
    }



    /*
     * RESPONSE PROCESSING
     *  Added empty checks
     */
    private void processResponse(List<Request> req) {
        for (Request request : req) {
            if (request.getType() == RequestType.LOGIN) {
                if (request.getStatus() == Status.SUCCESS) {
                    loggedIn = true;
                    responseMessage = "Login Successful";
                    userType = request.getUserType();
                } else {
                    System.out.println("Login failed");
                }
            } else if (request.getType() == RequestType.LOGOUT) {
                if (request.getStatus() == Status.SUCCESS) {
                    System.out.println("Logging out...");
                    createLogoutRequest(); // Graceful shutdown
                }
            } else if (request.getType() == RequestType.DEPOSIT) {
                if (request.getStatus() == Status.SUCCESS) {
                    isProcessing = false;
                    responseMessage = "Deposit Successful";
                }
            } else if (request.getType() == RequestType.WITHDRAW) {
                if (request.getStatus() == Status.SUCCESS) {
                    isProcessing = false;
                    responseMessage = "Withdraw Successful";
                } else if (request.getTexts() != null && !request.getTexts().isEmpty()) {
                    isProcessing = false;
                    responseMessage = request.getTexts().get(0);
                }
            } else if (request.getType() == RequestType.TRANSFER) {
                if (request.getStatus() == Status.SUCCESS) {
                    isProcessing = false;
                    responseMessage = "Transfer Successful";
                } else if (request.getTexts() != null && !request.getTexts().isEmpty()) {
                    isProcessing = false;
                    responseMessage = request.getTexts().get(0);
                }
            }
        }
    }


    /*
     * METHODS FOR GUI
     */
    public UserType getUserType() {
    	return userType;
    }
    public boolean getIsProcessing() {
        return isProcessing;
    }


    public String getResponseMessage() {
        return responseMessage;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public void createLoginRequest(String username, String password) {
        ArrayList<String> userAndPass = new ArrayList<>();
        userAndPass.add(username);
        userAndPass.add(password);
        Request loginRequest = new Request(userAndPass, RequestType.LOGIN, Status.REQUEST);
        List<Request> requests = new ArrayList<>();
        requests.add(loginRequest);

        outputHandler.enqueueRequest(requests);
    }

    public void createDepositRequest(double amount) {
        isProcessing = true;
        Request depositRequest = new Request(amount, RequestType.DEPOSIT, Status.REQUEST);
        List<Request> requests = new ArrayList<>();
        requests.add(depositRequest);
        outputHandler.enqueueRequest(requests);
    }

    public void createWithdrawRequest(double amount) {
        isProcessing = true;
        Request withdrawRequest = new Request(amount, RequestType.WITHDRAW, Status.REQUEST);
        List<Request> requests = new ArrayList<>();
        requests.add(withdrawRequest);
        outputHandler.enqueueRequest(requests);
    }

    public void createTransferRequest(int toAccountID, double amount) {
        isProcessing = true;
        ArrayList<String> ID = new ArrayList<>();
        ID.add(String.valueOf(toAccountID));
        Request transferRequest = new Request(ID, amount, RequestType.TRANSFER, Status.REQUEST);
        List<Request> requests = new ArrayList<>();
        requests.add(transferRequest);

        outputHandler.enqueueRequest(requests);
    }
    
    public void createFreezeRequest(int acc_ID) {
        isProcessing = true;
        ArrayList<String> ID = new ArrayList<>();
        ID.add(acc_ID + "");
        Request freezeRequest = new Request(ID, RequestType.FREEZE, Status.REQUEST);
        List<Request> requests = new ArrayList<>();
        requests.add(freezeRequest);
        outputHandler.enqueueRequest(requests);
    }




    public void createLogoutRequest() {
    	List<Request> requests = new ArrayList<>();
    	requests.add(new Request(RequestType.LOGOUT, Status.REQUEST));
    	outputHandler.enqueueRequest(requests);

	    // Shutdown
	    try {
	        System.out.println("Stopping Handlers...");
	        inputHandler.stop();
	        outputHandler.stop();
	        alive = false;
	
	        // Wait for threads to finish
	        Thread inputThread = new Thread(inputHandler);
	        Thread outputThread = new Thread(outputHandler);
	        inputThread.join();
	        outputThread.join();
	
	        // Close the socket and streams
	        if (inputHandler.getInputStream() != null) {
	            inputHandler.getInputStream().close();
	        }
	        if (outputHandler.getOutputStream() != null) {
	            outputHandler.getOutputStream().close();
	        }
	    } catch (IOException | InterruptedException e) {
	        e.printStackTrace();
	    }
}



}
