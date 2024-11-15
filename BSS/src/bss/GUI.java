package bss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

    private JFrame frame;
    private Client client; 

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
    
    private void handleLogin(String accountId, String pin) {
       
    }
    
 // display the teller view
    private void showTellerView() {
        
    }
    
 // display the customer view
    private void showCustomerView() {
       
    }


}