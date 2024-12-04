package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bss.Account;

class AccountTest {
	
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
    }

    // Check if account credentials are validated
    @Test
    void testCheckCredentials() {
        account.setPin("1234");
        boolean result = account.checkCredentials(account.getAccountID(), "1234");
        assertTrue(result);
    }

    // Check if frozen status can be set
    @Test
    void testSetFrozen() {
        account.setFrozen(true);
        assertTrue(account.getFrozen());
    }

    // Check if deposit works correctly
    @Test
    void testDeposit() {
        account.deposit(100.0);
        assertEquals(100.0, account.getAmount());
    }

    // Check if withdraw works correctly
    @Test
    void testWithdraw() {
        account.deposit(100.0);
        account.withdraw(50.0);
        assertEquals(50.0, account.getAmount());
    }

    // Check if users can be added to the account
    @Test
    void testAddUser() {
        account.addUser(1);
        assertTrue(account.getUsers().contains(1));
    }

    // Check if account type is set correctly
    @Test
    void testSetAccountType() {
        account.setAccountTypeCheckings();
        assertEquals(enums.AccountType.Checkings, account.getAccountType());
    }
    
    // Check if produced string is correctly formatted
    @Test
    void testPrintUsers() {
        account.addUser(1);
        account.addUser(2);
        String users = account.printUsers();
        assertEquals("Customer_ID 1,Customer_ID 2", users);
    }
    
    // Check if the account ID correctly updates
    @Test
    void testMatchUpAccountID() {
        account.matchUpAccountID(5);
        assertEquals(5, account.getAccountID());
    }
    
}

