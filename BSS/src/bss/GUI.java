package bss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import enums.UserType;
import network.Client;

public class GUI {

    private JFrame frame;
    private Client client;
    private UserType userType; 

    public GUI() {

    	this.client = client;
        initializeLoginScreen();
    }

    // initialize the login screen
    private void initializeLoginScreen() 
    {
    	
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
    
    private void handleLogin(String accountId, String pin) 
    {
    	
    	// notify the Client to send login request
        System.out.println("Login attempted with ID: " + accountId + " and PIN: " + pin);
    }
    
	public void setUserRole(String role) 
	{
	        
	    	this.userRole = role;
	        
	    	if (role.equalsIgnoreCase("teller")) 
	    	{
	            showTellerView();
	        } 
	    	else if (role.equalsIgnoreCase("customer")) 
	        {
	            showCustomerView();
	        } 
	    	else 
	    	{
	            JOptionPane.showMessageDialog(frame, "invalid role");
	        }
	}
    
	// display the teller view
    private void showTellerView() 
    {	
    	frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4, 1));

        JButton viewAccountsButton = new JButton("View Accounts");
        JButton createAccountButton = new JButton("Create Account");
        JButton readTransactionLogButton = new JButton("Read Transaction Log");
        JButton logoutButton = new JButton("Logout");
        
        viewAccountsButton.addActionListener(e -> handleViewAccounts());
        createAccountButton.addActionListener(e -> handleCreateAccount());
        readTransactionLogButton.addActionListener(e -> handleReadTransactionLog());
        logoutButton.addActionListener(e -> initializeLoginScreen());

        frame.add(viewAccountsButton);
        frame.add(createAccountButton);
        frame.add(readTransactionLogButton);
        frame.add(logoutButton);
        frame.revalidate();
        frame.repaint();
    }
    
    // display the customer view
    private void showCustomerView() 
    {
    	frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4, 1));

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton viewBalanceButton = new JButton("View Balance");
        JButton logoutButton = new JButton("Logout");
        
        depositButton.addActionListener(e -> handleDeposit());
        withdrawButton.addActionListener(e -> handleWithdraw());
        viewBalanceButton.addActionListener(e -> handleViewBalance());
        logoutButton.addActionListener(e -> initializeLoginScreen());

        frame.add(depositButton);
        frame.add(withdrawButton);
        frame.add(viewBalanceButton);
        frame.add(logoutButton);
        frame.revalidate();
        frame.repaint();
    }
    
    // Placeholder: handle View Accounts action
    private void handleViewAccounts() 
    {
    	frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Accounts:");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea accountsArea = new JTextArea();
        accountsArea.setEditable(false);
        accountsArea.setText("Account 1: $0.00\nAccount 2: $100.00"); // Replace with real data later

        JScrollPane scrollPane = new JScrollPane(accountsArea);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showTellerView());

        frame.add(headerLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    // Placeholder: handle Create Account action
    private void handleCreateAccount() 
    {
    	frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4, 2));
        

        JLabel accountTypeLabel = new JLabel("Select Account Type:");
        JComboBox<String> accountTypeDropdown = new JComboBox<>(new String[]{"Checkings", "Savings"});

        JLabel pinLabel = new JLabel("Set PIN:");
        JPasswordField pinField = new JPasswordField();

        JButton createButton = new JButton("Create");
        JButton backButton = new JButton("Back");

        createButton.addActionListener(e -> {
            String selectedType = (String) accountTypeDropdown.getSelectedItem();
            String pin = new String(pinField.getPassword());
            System.out.println("Creating account of type: " + selectedType + " with PIN: " + pin);
            // need to logic to notify Client class
        });

        backButton.addActionListener(e -> showTellerView());

        frame.add(accountTypeLabel);
        frame.add(accountTypeDropdown);
        frame.add(pinLabel);
        frame.add(pinField);
        frame.add(new JLabel()); 
        frame.add(new JLabel()); 
        frame.add(createButton);
        frame.add(backButton);
        frame.revalidate();
        frame.repaint();
    }

    // Placeholder: handle Read Transaction Log action
    private void handleReadTransactionLog() 
    {
    	frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Transaction Log:");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setText("Transaction 1: $50 deposit\nTransaction 2: $20 withdrawal"); // Replace with real data later
        JScrollPane scrollPane = new JScrollPane(logArea);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showTellerView());

        frame.add(headerLabel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    // Placeholder: handle Deposit action
    private void handleDeposit() 
    {
    	frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));

        JLabel depositLabel = new JLabel("Enter Deposit Amount:");
        JTextField depositField = new JTextField();

        JButton confirmButton = new JButton("Confirm");
        JButton backButton = new JButton("Back");

        confirmButton.addActionListener(e -> {
            String amountText = depositField.getText();
            try 
            {
                double amount = Double.parseDouble(amountText);
                System.out.println("Depositing amount: " + amount);
                // Add logic to notify Client class
            } 
            catch (NumberFormatException ex) 
            {
                JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.");
            }
        });

        backButton.addActionListener(e -> {
            
        	if ("customer".equalsIgnoreCase(userRole)) 
            {
                showCustomerView();
            } 
            else if ("teller".equalsIgnoreCase(userRole)) 
            {
                showTellerView();
            }
        });

        frame.add(depositLabel);
        frame.add(depositField);
        frame.add(new JLabel()); 
        frame.add(new JLabel()); 
        frame.add(confirmButton);
        frame.add(backButton);
        frame.revalidate();
        frame.repaint();
    }

    // Placeholder: handle Withdraw action
    private void handleWithdraw() 
    {
    	frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));

        JLabel withdrawLabel = new JLabel("Enter Withdrawal Amount:");
        JTextField withdrawField = new JTextField();

        JButton confirmButton = new JButton("Confirm");
        JButton backButton = new JButton("Back");

        confirmButton.addActionListener(e -> {
            String amountText = withdrawField.getText();
            try 
            {
                double amount = Double.parseDouble(amountText);
                System.out.println("Withdrawing amount: " + amount);
                // Add logic to notify Client class
            } 
            catch (NumberFormatException ex) 
            {
                JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.");
            }
        });

        backButton.addActionListener(e -> {
            
        	if ("customer".equalsIgnoreCase(userRole)) 
            {
                showCustomerView();
            } 
            else if ("teller".equalsIgnoreCase(userRole)) 
            {
                showTellerView();
            }
        });

        frame.add(withdrawLabel);
        frame.add(withdrawField);
        frame.add(new JLabel()); 
        frame.add(new JLabel()); 
        frame.add(confirmButton);
        frame.add(backButton);
        frame.revalidate();
        frame.repaint();
    }

    // Placeholder: handle View Balance action
    private void handleViewBalance() 
    {
    	frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(2, 1));

        JLabel balanceLabel = new JLabel("Current Balance: $0.00"); // Replace with dynamic data later
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            
        	if ("customer".equalsIgnoreCase(userRole)) 
        	{
                showCustomerView();
            } 
        	else if ("teller".equalsIgnoreCase(userRole)) 
        	{
                showTellerView();
            }
        });

        frame.add(balanceLabel);
        frame.add(backButton);
        frame.revalidate();
        frame.repaint();
    }

}