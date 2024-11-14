import java.util.ArrayList;

public class Customer extends User{
	protected ArrayList<Account> accounts;
	
	public Customer() {
		this.accounts = new ArrayList<Account>();
	}

	public Account getAccount(int in_id, String in_pin, int in_index) {
		//findAccount()?
		//stub code bc idk whats past here for now
		Account A = new Account();
		return A;
	}

	public void addAccount(Account acc) {
		accounts.add(acc);

	} 
	
}
