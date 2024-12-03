package bss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import enums.UserType;
import network.Client;

public class GUI implements Runnable {

	private JFrame frame;
	private Client client;
	private UserType userType;

	public GUI(Client client) {

		this.client = client;
	}

	// initialize the login screen
	public void run() {

		frame = new JFrame("Banking System - Login");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 200);
		frame.setLayout(new GridLayout(3, 2));

		JLabel accountIdLabel = new JLabel("Account ID:");
		JTextField accountIdField = new JTextField();
		JLabel pinLabel = new JLabel("PIN:");
		JPasswordField pinField = new JPasswordField();

		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(e -> {
			String username = accountIdField.getText();
			String password = new String(pinField.getPassword());
			handleLogin(username, password);
		});

		frame.add(accountIdLabel);
		frame.add(accountIdField);
		frame.add(pinLabel);
		frame.add(pinField);
		frame.add(new JLabel()); // Spacer
		frame.add(loginButton);
		frame.setVisible(true);
	}

	private synchronized void handleLogin(String username, String password) {

		client.createLoginRequest(username, password);
		loadingDots("Logging In");
//        JOptionPane.showMessageDialog(frame, "Logging in...");

		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				while (client.getIsProcessing()) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				return null;
			}

			@Override
			protected void done() {
				String response = client.getResponseMessage();
				JOptionPane.showMessageDialog(frame, response);

				if (client.getLoggedIn()) {
					userType = client.getUserType();
					showUserView();
				}
			}
		}.execute();
	}

	private synchronized void showUserView() {
		if (userType == UserType.TELLER) {
			showTellerView();
		} else if (userType == UserType.CUSTOMER) {
			showCustomerView(true);
		}
	}

	// display the teller view
	private synchronized void showTellerView() {
		frame.getContentPane().removeAll();
		frame.setTitle("Teller View");
		frame.setLayout(new GridLayout(4, 1));

		JButton enterAccountButton = new JButton("Enter Account");
		JButton freezeAccountButton = new JButton("Freeze Account");
		JButton unfreezeAccountButton = new JButton("Unfreeze Account");
		JButton readLogsButton = new JButton("Read Logs");
		JButton CreateCustomerButton = new JButton("Create Customer");
		JButton CreateAccountButton = new JButton("Create Account");
		JButton logoutButton = new JButton("Logout");

		enterAccountButton.addActionListener(e -> handleEnterAccount());
		freezeAccountButton.addActionListener(e -> handleFreezeAccount());
		unfreezeAccountButton.addActionListener(e -> handleUnfreezeAccount());
		readLogsButton.addActionListener(e -> handleReadLogs());
		CreateCustomerButton.addActionListener(e -> handleCreateCustomer());
		CreateAccountButton.addActionListener(e -> handleCreateAccount());
		logoutButton.addActionListener(e -> handleLogout());

		frame.add(enterAccountButton);
		frame.add(freezeAccountButton);
		frame.add(unfreezeAccountButton);
		frame.add(readLogsButton);
		frame.add(CreateCustomerButton);
		frame.add(CreateAccountButton);
		frame.add(logoutButton);
		frame.revalidate();
		frame.repaint();
	}

	private synchronized void handleCreateAccount() {
		String password = JOptionPane.showInputDialog(frame, "Enter Password:");
		String cust_id = JOptionPane.showInputDialog(frame, "Enter Customer ID to be Added:");
		if (password != null && cust_id != null) {
			client.createAccountCreationRequest(password, cust_id);
			loadingDots("Creating Account");
			JOptionPane.showMessageDialog(frame, client.getResponseMessage());
		}
	}

	private synchronized void handleCreateCustomer() {

		client.createCustomerCreationRequest();
		loadingDots("Creating Customer");
		JOptionPane.showMessageDialog(frame, client.getResponseMessage());

	}

	// display the customer view
	private synchronized void showCustomerView(boolean customer) {
		frame.getContentPane().removeAll();
		frame.setTitle("Customer View");
		frame.setLayout(new GridLayout(4, 1));
		JLabel balance = new JLabel("Balance: $" + client.getBal());
		JButton depositButton = new JButton("Deposit");
		JButton withdrawButton = new JButton("Withdraw");
		JButton transferButton = new JButton("Transfer");
		JButton logoutButton;
		if(customer) {
			logoutButton = new JButton("Logout");
		}
		else {
			logoutButton = new JButton("Leave");
		}
		

		depositButton.addActionListener(e -> handleDeposit());
		withdrawButton.addActionListener(e -> handleWithdraw());
		transferButton.addActionListener(e -> handleTransfer());
		logoutButton.addActionListener(e -> handleLogout());
		
		frame.add(balance);
		frame.add(depositButton);
		frame.add(withdrawButton);
		frame.add(transferButton);
		frame.add(logoutButton);
		frame.revalidate();
		frame.repaint();
	}

	private synchronized void handleEnterAccount() {
		String accountId = JOptionPane.showInputDialog(frame, "Enter Account ID:");
		if (accountId != null) {
			client.createEnterAccountRequest(Integer.parseInt(accountId));
			loadingDots("Entering Account");
			JOptionPane.showMessageDialog(frame, client.getResponseMessage());
			if (client.getAccountAccessed()) {
				showCustomerView(false);
			}
		}
	}

	private synchronized void handleFreezeAccount() {
		String accountId = JOptionPane.showInputDialog(frame, "Enter Account ID to Freeze:");
		if (accountId != null) {
			client.createFreezeRequest(Integer.parseInt(accountId));
			loadingDots("Freezing Account");
			JOptionPane.showMessageDialog(frame, client.getResponseMessage());
		}
	}
	
	private synchronized void handleUnfreezeAccount() {
		String accountId = JOptionPane.showInputDialog(frame, "Enter Account ID to Unfreeze:");
		if (accountId != null) {
			client.createUnfreezeRequest(Integer.parseInt(accountId));
			loadingDots("Unfreezing Account");
			JOptionPane.showMessageDialog(frame, client.getResponseMessage());
		}
	}

	private synchronized void handleReadLogs() {
		String startDate = JOptionPane.showInputDialog(frame, "Enter Start Date (yyyy-MM-ddTHH:mm:ss):");
		String endDate = JOptionPane.showInputDialog(frame, "Enter End Date (yyyy-MM-ddTHH:mm:ss):");
		if (startDate != null && endDate != null) {
			client.createReadLogsRequest(startDate, endDate);
			loadingDots("Reading Logs");
			JOptionPane.showMessageDialog(frame, "Fetching logs...");

			new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() {
					while (client.getIsProcessing()) {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
					return null;
				}

				@Override
				protected void done() {
					JOptionPane.showMessageDialog(frame, client.getResponseMessage());
				}
			}.execute();
		}
	}

	private synchronized void handleDeposit() {
		String amount = JOptionPane.showInputDialog(frame, "Enter Deposit Amount:");

		if (amount != null) {
			client.createDepositRequest(Double.parseDouble(amount));
			loadingDots("Depositing");
			JOptionPane.showMessageDialog(frame, client.getResponseMessage());
			if (client.getAccountAccessed()) {
				showCustomerView(false);
			}
			else {
				showCustomerView(true);
			}
		}
	}

	private synchronized void handleWithdraw() {
		String amount = JOptionPane.showInputDialog(frame, "Enter Withdrawal Amount:");
		if (amount != null) {
			client.createWithdrawRequest(Double.parseDouble(amount));
			loadingDots("Withdrawing");
			JOptionPane.showMessageDialog(frame, client.getResponseMessage());
			if (client.getAccountAccessed()) {
				showCustomerView(false);
			}
			else {
				showCustomerView(true);
			}
		}
	}

	private synchronized void handleTransfer() {
		String accountId = JOptionPane.showInputDialog(frame, "Enter Account ID to Transfer To:");
		String amount = JOptionPane.showInputDialog(frame, "Enter Transfer Amount:");
		if (accountId != null && amount != null) {
			client.createTransferRequest(Integer.parseInt(accountId), Double.parseDouble(amount));
			loadingDots("Transferring");
			JOptionPane.showMessageDialog(frame, client.getResponseMessage());
			if (client.getAccountAccessed()) {
				showCustomerView(false);
			}
			else {
				showCustomerView(true);
			}
		}
	}

	/*
	 * // Placeholder: handle View Balance action private void handleViewBalance() {
	 * frame.getContentPane().removeAll(); frame.setLayout(new GridLayout(2, 1));
	 * 
	 * JLabel balanceLabel = new JLabel("Current Balance: $0.00"); // Replace with
	 * dynamic data later
	 * balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
	 * 
	 * JButton backButton = new JButton("Back"); backButton.addActionListener(e -> {
	 * 
	 * if ("customer".equalsIgnoreCase(userRole)) { showCustomerView(); } else if
	 * ("teller".equalsIgnoreCase(userRole)) { showTellerView(); } });
	 * 
	 * frame.add(balanceLabel); frame.add(backButton); frame.revalidate();
	 * frame.repaint(); }
	 */

	private synchronized void handleLogout() {
		client.createLogoutRequest();
		loadingDots("Logging Out");
		JOptionPane.showMessageDialog(frame, "Logged out successfully.");
		frame.dispose();
	}

	private void loadingDots(String message) {

		JLabel loading = new JLabel(message);
		JOptionPane pane = new JOptionPane(loading);
		JDialog dialog = pane.createDialog(message);

		while (client.getIsProcessing()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println("Thread was interrupted.");
			}
			loading.setText(message += ".");
		}
	}

}