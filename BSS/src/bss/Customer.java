package bss;
import java.util.ArrayList;

public class Customer extends User{
	protected ArrayList<Account> accounts;
	
	public Customer() {
		this.accounts = new ArrayList<Account>();
	}
	//this constructor used when reading from bank data
	public Customer(int in_id) {
		matchUpCustomerID(in_id);
		this.accounts = new ArrayList<Account>();
	}
	//pass in Customer id read in from .txt file
    public void matchUpCustomerID(int in_id) {
    	//do nothing if count == id, 
    	if (count == in_id) {
    		return;
    	}
    	// if after new Customer(), count is not equal to in_id, increment until it is.
    	// this is to consider the deleted Customers from a previous session as we make new Customers and assign IDs w/ static
    	// count as we read them in from our .txt file 
    	// w/o this, Customers "desync" their assigned IDs from previous session if less Customers exist now then in previous session
    	while(count != in_id) {
    		count++;
    	}
    	setID(count);
    	return;
    }
    //not sure if we need index
	public Account getAccount(int in_id, String in_pin) { //,int in_index) {
		Account A =  new Account();
		for (int i = 0; i < accounts.size(); i++ ) {
			if (accounts.get(i).getAccountID() == in_id && accounts.get(i).getPin() == in_pin) {
				A = accounts.get(i);
				break;
			}
			else { A = null; }
		}
		return A;
	}

	public void addAccount(Account acc) {
		accounts.add(acc);

	} 
	
	public int getAccountsSize() {
		return this.accounts.size();
	}
	
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	
}
