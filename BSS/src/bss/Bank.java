package bss;
import java.util.ArrayList;


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
	
	// find a customer by customer ID
	public Customer findCustomer(int customerID) {

		for (Customer customer : customers) 
		{
			// Assuming each customer has a unique ID and a getID() method
            if (customer.getID() == customerID) 
            {
                return customer;
            }
        }
		
        return null;
    }
	
	// Find a teller by teller ID
	public Teller findTeller(int teller_id) {
        
		for (Teller teller : tellers) 
		{
			// assuming each teller has a unique ID and a getID() method
            if (teller.getID() == tellerID) 
            {
                return teller;
            }
        }
		
        return null;
    }
	
	// save bank data to a file
	public void saveData() {
		
	}
	
	// load bank data from a file
	public void loadData() {
		
	}

}
