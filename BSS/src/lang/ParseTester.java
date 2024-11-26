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
	            // Read from a file												//insert full file path before "/" to test for now /Users/edgarromero/eclipse-workspace/Banking-System/BSS/src/TestCustomers.txt
	            ArrayList<Customer> customers = Parser.load(B.getCustomers(), B.getAccounts(), "/Users/edgarromero/eclipse-workspace/Banking-System/BSS/src/TestCustomers.txt");
	            // change data
	            //ArrayList<Account> accounts = B.getAccounts();
	            System.out.println("Customers loaded: " + customers.size());
	            System.out.println("Accounts loaded: " + B.getAccounts().size());
	            // writing back to file
	            
	            //remove b.get accounts
	           Parser.writeToFile(customers, "output.txt");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
