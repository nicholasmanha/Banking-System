package bss;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Bank {
	private ArrayList<Account> accounts;
	private ArrayList<Customer> customers;
	private ArrayList<Teller> tellers;
	
	public Bank() {
		this.accounts = new ArrayList<>();
		this.customers = new ArrayList<>();
		this.tellers = new ArrayList<>();
	}
	// !!! these is just for debugging purposes, will remove in production
	public ArrayList<Account> getAccounts() {
		return this.accounts;
	}
	public ArrayList<Customer> getCustomers() {
		return this.customers;
	}
	
	public ArrayList<Teller> getTellers() {
		return this.tellers;
	}
	
	// add an account to the bank
	public void addAccount(Account account) {
		this.accounts.add(account);
	}
	// add a customer to the bank
	public void addCustomer(Customer customer) {
		customers.add(customer);
	}
	// add a teller to the bank
	public void addTeller(Teller teller) {
		tellers.add(teller);
	}
	// find an account by account ID
	public Account findAccount(int account_id, String account_password){
		for (Account account : accounts) 
		{
	        if (account.checkCredentials(account_id, account_password)) 
	        {
	            return account;
	        }
	    }
		
	    return null;
	}
	public Teller findTeller(int teller_id, String teller_password){
		for (Teller teller : tellers) 
		{
	        if (teller.checkCredentials(teller_id, teller_password)) 
	        {
	            return teller;
	        }
	    }
	    return null;
	}

	// save bank data to a file
	public void saveData() {
		
	}
	
	public void loadData() {
		
	}
}