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

    public GUI(Client client) {

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
    
    private void handleLogin(String username, String password) 
    {
    	
    	client.createLoginRequest(username, password);
        JOptionPane.showMessageDialog(frame, "Logging in...");
        
        new SwingWorker<Void, Void>() 
        {
            @Override
            protected Void doInBackground() 
            {
                while (client.getIsProcessing()) 
                {
                    try 
                    {
                        Thread.sleep(200);
                    } 
                    catch (InterruptedException e) 
                    {
                        Thread.currentThread().interrupt();
                    }
                }
                return null;
            }

            @Override
            protected void done() 
            {
                String response = client.getResponseMessage();
                JOptionPane.showMessageDialog(frame, response);
                userType = client.getUserType();

                if (client.getLoggedIn()) 
                {
                    if (userType == UserType.TELLER) 
                    {
                        showTellerView();
                    } 
                    else if (userType == UserType.CUSTOMER) 
                    {
                        showCustomerView();
                    }
                }
            }
        }.execute();
    }
    
	// display the teller view
    private void showTellerView() 
    {	
    	frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4, 1));

        JButton enterAccountButton = new JButton("Enter Account");
        JButton freezeAccountButton = new JButton("Freeze Account");
        JButton readLogsButton = new JButton("Read Logs");
        JButton logoutButton = new JButton("Logout");
        
        enterAccountButton.addActionListener(e -> handleEnterAccount());
        freezeAccountButton.addActionListener(e -> handleFreezeAccount());
        readLogsButton.addActionListener(e -> handleReadLogs());
        logoutButton.addActionListener(e -> handleLogout());

        frame.add(enterAccountButton);
        frame.add(freezeAccountButton);
        frame.add(readLogsButton);
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
    
    private void handleEnterAccount() 
    {
        String accountId = JOptionPane.showInputDialog(frame, "Enter Account ID:");
        if (accountId != null) 
        {
            client.createEnterAccountRequest(Integer.parseInt(accountId));
            JOptionPane.showMessageDialog(frame, client.getResponseMessage());
        }
    }

    private void handleFreezeAccount() 
    {
        String accountId = JOptionPane.showInputDialog(frame, "Enter Account ID to Freeze:");
        if (accountId != null) 
        {
            client.createFreezeRequest(Integer.parseInt(accountId));
            JOptionPane.showMessageDialog(frame, client.getResponseMessage());
        }
    }

    private void handleReadLogs() 
    {
        String startDate = JOptionPane.showInputDialog(frame, "Enter Start Date (yyyy-MM-ddTHH:mm:ss):");
        String endDate = JOptionPane.showInputDialog(frame, "Enter End Date (yyyy-MM-ddTHH:mm:ss):");
        if (startDate != null && endDate != null) 
        {
            client.createReadLogsRequest(startDate, endDate);
            JOptionPane.showMessageDialog(frame, "Fetching logs...");

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() 
                {
                    while (client.getIsProcessing()) 
                    {
                        try 
                        {
                            Thread.sleep(200);
                        } 
                        catch (InterruptedException e) 
                        {
                            Thread.currentThread().interrupt();
                        }
                    }
                    return null;
                }

                @Override
                protected void done() 
                {
                    JOptionPane.showMessageDialog(frame, client.getResponseMessage());
                }
            }.execute();
        }
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
    
    private void handleLogout() 
    {
        client.createLogoutRequest();
        JOptionPane.showMessageDialog(frame, "Logged out successfully.");
        initializeLoginScreen();
    }

}