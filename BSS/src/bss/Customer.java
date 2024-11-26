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
    	if (count == in_id) {
    		this.setID(in_id);
    		count++;
    		return;
    	}
    	// if after new Customer(), count is <  in_id, make count == in_id
    	while (count <= in_id) {
    		this.setID(in_id);
    		count++;
    		return;
    	}
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
	
	public void setAccounts(ArrayList<Account> in_accounts) {
		this.accounts = in_accounts;
	}

}
