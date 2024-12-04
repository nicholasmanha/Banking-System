package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bss.Account;
import bss.Bank;
import bss.Customer;
import bss.Teller;

class BankTest {
	
    private Bank bank;
    private Account account;
    private Customer customer;
    private Teller teller;

    
    @BeforeEach
    void setUp() {
        bank = new Bank();
        account = new Account();
        customer = new Customer();
        teller = new Teller("password");
    }

    // Check if an account can be added to the bank
    @Test
    void testAddAccount() {
        bank.addAccount(account);
        assertTrue(bank.getAccounts().contains(account));
    }

    // Check if a customer can be added to the bank
    @Test
    void testAddCustomer() {
        bank.addCustomer(customer);
        assertTrue(bank.getCustomers().contains(customer));
    }

    // Check if a teller can be added to the bank
    @Test
    void testAddTeller() {
        bank.addTeller(teller);
        assertTrue(bank.getTellers().contains(teller));
    }

    // Check if an account can be found by ID
    @Test
    void testFindAccount() {
        bank.addAccount(account);
        assertEquals(account, bank.findAccount(account.getAccountID()));
    }

    // Check if a customer can be found by ID
    @Test
    void testFindCustomer() {
        bank.addCustomer(customer);
        assertEquals(customer, bank.findCustomer(customer.getId()));
    }

    // Check if a teller can be found by ID
    @Test
    void testFindTeller() {
        bank.addTeller(teller);
        assertEquals(teller, bank.findTeller(teller.getId()));
    }
}

