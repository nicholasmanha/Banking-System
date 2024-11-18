package bss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

    private JFrame frame;
    private String userRole; 

    public GUI() {

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
        loginButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String accountId = accountIdField.getText();
                String pin = new String(pinField.getPassword());
                handleLogin(accountId, pin);
                
            }
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
        // logic for viewing accounts goes here
    }

    // Placeholder: handle Create Account action
    private void handleCreateAccount() 
    {
        // logic for creating an account goes here
    }

    // Placeholder: handle Read Transaction Log action
    private void handleReadTransactionLog() 
    {
        // Logic for reading transaction logs goes here
    }

    // Placeholder: handle Deposit action
    private void handleDeposit() 
    {
        // logic for depositing money goes here
    }

    // Placeholder: handle Withdraw action
    private void handleWithdraw() 
    {
        // logic for withdrawing money goes here
    }

    // Placeholder: handle View Balance action
    private void handleViewBalance() 
    {
        // logic for viewing balance goes here
    }

}