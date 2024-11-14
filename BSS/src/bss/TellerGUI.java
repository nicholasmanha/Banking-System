package bss;

public class TellerGUI implements GUI{
	
	private Bank bank;
	private Teller teller;
	
	public TellerGUI(Bank bank, Teller teller) {
		this.bank = bank;
		this.teller = teller;
		
	}
	
	public void processCommands() {
		//need to add menu options for teller
	}

}
