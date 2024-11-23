package lang;

import java.io.IOException;
import java.util.ArrayList;

import bss.Customer;
import bss.Account;
import bss.Bank;

public class ParseTester {
	    public static void main(String[] args) {
	    	Bank B = new Bank();
	    	
	        try {
	            // Read from a file												//insert full file path before "/" to test for now 
	            ArrayList<Customer> customers = Parser.readFromFile("/TestCustomers.txt", B.getAccounts());
	            // change data
	            System.out.println("Customers loaded: " + customers.size());
	            System.out.println("Accounts loaded: " + B.getAccounts().size());
	            // writing back to file
	           // Parser.writeToFile(customers, "output.txt");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
