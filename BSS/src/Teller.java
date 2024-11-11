
public class Teller{
	
	
	
	
	
	public Account createAccount(String pin) {
		Account newAccount = new Account();
		newAccount.setPin(pin);
		return newAccount;
		
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
	
	
	
	
}