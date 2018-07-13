package uw.edu.fountainejf;


import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountFactory;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactory;
import java.util.Arrays;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class DaoTest {
    private static final String MEGA_CITY = "Mega City";
    private static final String ILLINOIS_STATE_CODE = "IL";
    private static final String MEGA_CITY_ZIP = "60666";
    private static final String NEO_PHONE_NUMBER = "(555) 567-8900";
    private static final String NEO_STREET_ADDRESS = "101 Hackett Street";
    private static final String NEO_ACCOUNT_NAME = "neotheone";
    private static final byte[] NEO_ACCOUNT_PASSWD = new byte[]{112, 97, 115, 115, 119, 111, 114, 100};
    private static final int NEO_ACCOUNT_INIT_BALANCE = 100000;
    private static final String NEO_ACCOUNT_FULL_NAME = "Thomas Anderson";
    private static final String NEO_EMAIL_ADDRESS = "neo@metacortex.com";
    private static final String NEO_CREDIT_CARD_ISSUER = "Commonwealth Bank";
    private static final String NEO_CREDIT_CARD_HOLDER = "Thomas A Anderson";
    private static final String NEO_CREDIT_CARD_NUM = "1234-5678-9012-3456";
    private static final String NEO_CREDIT_CARD_EXPIRES = "03/05";
    private static final String ALICE_ACCOUNT_NAME = "aabernathy";
    private static final byte[] ALICE_ACCOUNT_PASSWD = new byte[]{112, 97, 115, 115, 119, 111, 114, 100, 50};
    private static final int ALICE_ACCOUNT_INIT_BALANCE = 500000;
    private static final String ALICE_ACCOUNT_FULL_NAME = "Alice Abernathy";
    private static final String ALICE_EMAIL_ADDRESS = "alice@umbrella.com";
    private static final String ALICE_CREDIT_CARD_ISSUER = "Global Bank";
    private static final String ALICE_CREDIT_CARD_HOLDER = "A Abernathy";
    private static final String ALICE_CREDIT_CARD_NUM = "5678-9012-3456-1234";
    private static final String ALICE_CREDIT_CARD_EXPIRES = "02/06";
    private static final String PEPPER_ACCOUNT_NAME = "pepper_potts";
    private static final byte[] PEPPER_ACCOUNT_PASSWD = new byte[]{116, 111, 110, 121, 115, 97, 112, 97, 105, 110};
    private static final int PEPPER_ACCOUNT_INIT_BALANCE = 200000;
    private static final String PEPPER_ACCOUNT_FULL_NAME = "Pepper Potts";
    private static final String PEPPER_PHONE_NUMBER = "(555) 654-3210";
    private static final String PEPPER_EMAIL_ADDRESS = "pepper@stark.com";
    private static final String PEPPER_STREET_ADDRESS = "Stark Tower";
    private static final String PEPPER_CREDIT_CARD_ISSUER = "Avenger Bank";
    private static final String PEPPER_CREDIT_CARD_HOLDER = "Virginia Potts";
    private static final String PEPPER_CREDIT_CARD_NUM = "7890-1234-5678-9000";
    private static final String PEPPER_CREDIT_CARD_EXPIRES = "04/06";
    private static final String RACHAEL_ACCOUNT_NAME = "rachaelnex";
    private static final byte[] RACHAEL_ACCOUNT_PASSWD = new byte[]{13, 10, 9, 44, 32, 61, 58, 34, 46};
    private static final int RACHAEL_ACCOUNT_INIT_BALANCE = 500000;
    private static final String RACHAEL_ACCOUNT_FULL_NAME = "Rachael";
    private static final String RACHAEL_PHONE_NUMBER = "(555) 987-6543";
    private static final String RACHAEL_EMAIL_ADDRESS = "rachael@tyrell.com";
    private Account neoAcct;
    private Account aliceAcct;
    private Account pepperAcct;
    private Account rachaelAcct;
    private AccountFactory accountFactory;
    private DaoFactory daoFactory;
    private ClassPathXmlApplicationContext appContext;
    private AccountDao dao;

    public DaoTest() {
    }

    @Before
    public void setUp() throws Exception {
        this.appContext = new ClassPathXmlApplicationContext("context.xml");
        this.accountFactory = (AccountFactory)this.appContext.getBean("AccountFactory", AccountFactory.class);
        this.daoFactory = (DaoFactory)this.appContext.getBean("DaoFactory", DaoFactory.class);
        this.accountFactory = (AccountFactory)this.appContext.getBean("AccountFactory", AccountFactory.class);
        if (this.accountFactory == null) {
            throw new Exception("Unable to create account factory!");
        } else {
            this.neoAcct = this.accountFactory.newAccount("neotheone", NEO_ACCOUNT_PASSWD, 100000);
            if (this.neoAcct == null) {
                throw new Exception("Factory unable to create account!");
            } else {
                this.neoAcct.setFullName("Thomas Anderson");
                this.neoAcct.setPhone("(555) 567-8900");
                this.neoAcct.setEmail("neo@metacortex.com");
                Address addr = (Address)this.appContext.getBean("Address", Address.class);
                addr.setStreetAddress("101 Hackett Street");
                addr.setCity("Mega City");
                addr.setState("IL");
                addr.setZipCode("60666");
                this.neoAcct.setAddress(addr);
                CreditCard card = (CreditCard)this.appContext.getBean("CreditCard", CreditCard.class);
                card.setIssuer("Commonwealth Bank");
                card.setHolder("Thomas A Anderson");
                card.setAccountNumber("1234-5678-9012-3456");
                card.setExpirationDate("03/05");
                this.neoAcct.setCreditCard(card);
                this.aliceAcct = this.accountFactory.newAccount("aabernathy", ALICE_ACCOUNT_PASSWD, 500000);
                this.aliceAcct.setFullName("Alice Abernathy");
                this.aliceAcct.setPhone("(555) 567-8900");
                this.aliceAcct.setEmail("alice@umbrella.com");
                addr = (Address)this.appContext.getBean("Address", Address.class);
                addr.setStreetAddress("101 Hackett Street");
                addr.setCity("Mega City");
                addr.setState("IL");
                addr.setZipCode("60666");
                this.aliceAcct.setAddress(addr);
                card = (CreditCard)this.appContext.getBean("CreditCard", CreditCard.class);
                card.setIssuer("Global Bank");
                card.setHolder("A Abernathy");
                card.setAccountNumber("5678-9012-3456-1234");
                card.setExpirationDate("02/06");
                this.aliceAcct.setCreditCard(card);
                this.pepperAcct = this.accountFactory.newAccount("pepper_potts", PEPPER_ACCOUNT_PASSWD, 200000);
                this.pepperAcct.setFullName("Pepper Potts");
                this.pepperAcct.setPhone("(555) 654-3210");
                this.pepperAcct.setEmail("pepper@stark.com");
                addr = (Address)this.appContext.getBean("Address", Address.class);
                addr.setStreetAddress("Stark Tower");
                addr.setCity("Mega City");
                addr.setState("IL");
                addr.setZipCode("60666");
                this.pepperAcct.setAddress(addr);
                card = (CreditCard)this.appContext.getBean("CreditCard", CreditCard.class);
                card.setIssuer("Avenger Bank");
                card.setHolder("Virginia Potts");
                card.setAccountNumber("7890-1234-5678-9000");
                card.setExpirationDate("04/06");
                this.pepperAcct.setCreditCard(card);
                this.rachaelAcct = this.accountFactory.newAccount("rachaelnex", RACHAEL_ACCOUNT_PASSWD, 500000);
                this.rachaelAcct.setFullName("Rachael");
                this.rachaelAcct.setPhone("(555) 987-6543");
                this.rachaelAcct.setEmail("rachael@tyrell.com");
                this.dao = this.daoFactory.getAccountDao();
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        if (this.appContext != null) {
            this.appContext.close();
        }

        if (this.dao != null) {
            this.dao.close();
        }

    }

    private void compareAccounts(Account expected, Account actual) {
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertTrue(Arrays.equals(expected.getPasswordHash(), actual.getPasswordHash()));
        Assert.assertEquals((long)expected.getBalance(), (long)actual.getBalance());
        Assert.assertEquals(expected.getFullName(), actual.getFullName());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());
        Assert.assertEquals(expected.getEmail(), actual.getEmail());
        Address expectedAddr = expected.getAddress();
        Address actualAddr = actual.getAddress();
        if (expectedAddr == null) {
            Assert.assertNull("Expected the Address to be null", actualAddr);
        } else {
            Assert.assertEquals(expectedAddr.getStreetAddress(), actualAddr.getStreetAddress());
            Assert.assertEquals(expectedAddr.getCity(), actualAddr.getCity());
            Assert.assertEquals(expectedAddr.getState(), actualAddr.getState());
            Assert.assertEquals(expectedAddr.getZipCode(), actualAddr.getZipCode());
        }

        CreditCard expectedCc = expected.getCreditCard();
        CreditCard actualCc = actual.getCreditCard();
        if (expectedCc == null) {
            Assert.assertNull("Expected the CreditCard to be null", actualCc);
        } else {
            Assert.assertEquals(expectedCc.getIssuer(), actualCc.getIssuer());
            Assert.assertEquals(expectedCc.getHolder(), actualCc.getHolder());
            Assert.assertEquals(expectedCc.getAccountNumber(), actualCc.getAccountNumber());
            Assert.assertEquals(expectedCc.getExpirationDate(), actualCc.getExpirationDate());
        }

    }

    @Test
    public void testSetGet() throws Exception {
        this.dao.setAccount(this.neoAcct);
        this.dao.setAccount(this.aliceAcct);
        this.dao.setAccount(this.pepperAcct);
        this.dao.setAccount(this.rachaelAcct);
        Account acct = this.dao.getAccount("neotheone");
        this.compareAccounts(this.neoAcct, acct);
        acct = this.dao.getAccount("aabernathy");
        this.compareAccounts(this.aliceAcct, acct);
        acct = this.dao.getAccount("pepper_potts");
        this.compareAccounts(this.pepperAcct, acct);
        acct = this.dao.getAccount("rachaelnex");
        this.compareAccounts(this.rachaelAcct, acct);
        this.neoAcct.setAddress((Address)null);
        this.dao.setAccount(this.neoAcct);
        acct = this.dao.getAccount("neotheone");
        Assert.assertNull(acct.getAddress());
        this.neoAcct.setCreditCard((CreditCard)null);
        this.dao.setAccount(this.neoAcct);
        acct = this.dao.getAccount("neotheone");
        Assert.assertNull(acct.getCreditCard());
    }

    @Test
    public void testReload() throws Exception {
        this.dao.setAccount(this.neoAcct);
        this.dao.setAccount(this.aliceAcct);
        this.dao.setAccount(this.pepperAcct);
        this.dao.setAccount(this.rachaelAcct);
        this.dao = this.daoFactory.getAccountDao();
        Account acct = this.dao.getAccount("neotheone");
        this.compareAccounts(this.neoAcct, acct);
        acct = this.dao.getAccount("aabernathy");
        this.compareAccounts(this.aliceAcct, acct);
        acct = this.dao.getAccount("pepper_potts");
        this.compareAccounts(this.pepperAcct, acct);
        acct = this.dao.getAccount("rachaelnex");
        this.compareAccounts(this.rachaelAcct, acct);
        Assert.assertTrue(!this.neoAcct.getAddress().equals(this.pepperAcct.getAddress()));
        Assert.assertTrue(!this.neoAcct.getCreditCard().equals(this.pepperAcct.getCreditCard()));
    }

    @Test
    public void testDelete() throws Exception {
        this.dao.setAccount(this.neoAcct);
        this.dao.setAccount(this.aliceAcct);
        this.dao.setAccount(this.pepperAcct);
        this.dao.setAccount(this.rachaelAcct);
        Account acct = this.dao.getAccount("aabernathy");
        this.compareAccounts(this.aliceAcct, acct);
        this.dao.deleteAccount("aabernathy");
        acct = this.dao.getAccount("aabernathy");
        Assert.assertNull(acct);
    }

    @Test
    public void testReset() throws Exception {
        this.dao.setAccount(this.neoAcct);
        this.dao.setAccount(this.aliceAcct);
        this.dao.setAccount(this.pepperAcct);
        this.dao.setAccount(this.rachaelAcct);
        this.dao.getAccount("aabernathy");
        this.dao.reset();
        Account acct = this.dao.getAccount("neotheone");
        Assert.assertNull(acct);
        acct = this.dao.getAccount("aabernathy");
        Assert.assertNull(acct);
        acct = this.dao.getAccount("pepper_potts");
        Assert.assertNull(acct);
        acct = this.dao.getAccount("rachaelnex");
        Assert.assertNull(acct);
    }

    @Test
    public void testClose() throws Exception {
        this.dao.setAccount(this.neoAcct);
        this.dao.setAccount(this.aliceAcct);
        this.dao.setAccount(this.pepperAcct);
        this.dao.setAccount(this.rachaelAcct);
        this.dao.getAccount("aabernathy");
    }
}
