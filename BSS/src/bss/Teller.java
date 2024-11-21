package bss;

public class Teller extends User{

	private String password;
	public Teller(String password) {
		this.password = password;
	}
	
	public Account createAccount(String pin) {
		Account newAccount = new Account();
		newAccount.setPin(pin);
		return newAccount;

	}
	
	public boolean checkCredentials(int teller_id, String teller_password) {
		if(this.id == teller_id && this.password == teller_password) {
			return true;
		}
		else {
			return false;
		}
	}

	public void freezeAccount(Account acc) {
		acc.setFrozen(true);

	}

	public void addAccountToCustomer(Customer cust, Account acc) {
		cust.addAccount(acc);
	}

	public String readLog(int id) {

		return "log";
	}

	public String recoverPin(Account acc) {
		return acc.getPin();
	}

}
