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
	// add an account to the bank
	public void addAccount(Account account) {
		accounts.add(account);
	}
	// add an customer to the bank
	public void addAccount(Customer customer) {
		customers.add(customer);
	}
	// add an teller to the bank
	public void addAccount(Teller teller) {
		tellers.add(teller);
	}
	// find an account by account ID
	public Account findAccount(int account_id){
		for (Account account : accounts) 
		{
	        if (account.getAccountID() == account_id) 
	        {
	            return account;
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