package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bss.Account;
import bss.ATM;

class ATMTest {
	
    private ATM atm;
    private Account account;

    @BeforeEach
    void setUp() {
        atm = new ATM();
        account = new Account();
    }

    // Check if session starts correctly
    @Test
    void testLogIn() {
        assertNotNull(atm.logIn(account));
    }

    // Check if session ends correctly
    @Test
    void testLogOut() {
    	atm.logIn(account);
        atm.logOut();
        // Verify the session's account is null 
        try {
            atm.getBal();
            fail("Expected NullPointerException due to logged-out state.");
        } catch (NullPointerException e) {
            assertTrue(true, "ATM session is invalid after logging out.");
        }
    }

    // Check if deposit works
    @Test
    void testDeposit() {
        atm.logIn(account);
        atm.deposit(200.0);
        assertEquals(200.0, atm.getBal());
    }

    // Check if withdraw works
    @Test
    void testWithdraw() {
        atm.logIn(account);
        atm.deposit(200.0);
        atm.withdraw(50.0);
        assertEquals(150.0, atm.getBal());
    }

    // Check if transfer works
    @Test
    void testTransfer() {
        Account targetAccount = new Account();
        atm.logIn(account);
        atm.deposit(200.0);
        atm.transfer(targetAccount, 100.0);
        assertEquals(100.0, atm.getBal());
        assertEquals(100.0, targetAccount.getAmount());
    }
   
    // Check if getBalance works
    @Test
    void testGetBal() {
        atm.logIn(account);
        account.deposit(500.0);
        assertEquals(500.0, atm.getBal(), 0.01);
    }
    
}

