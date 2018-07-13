package uw.edu.fountainejf;


import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountFactory;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import java.util.Arrays;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class accountTest {
    private static final String ACCT_NAME = "neotheone";
    private static final String ALT_ACCT_NAME = "tanderson";
    private static final String BAD_ACCT_NAME = "theone";
    private static final byte[] PASSWORD_BYTES = new byte[]{112, 97, 115, 115, 119, 111, 114, 100};
    private static final byte[] PASSWORD_BYTES_UPDATED = new byte[]{98, 101, 116, 116, 101, 114, 112, 97, 115, 115, 119, 111, 114, 100};
    private static final int INIT_BALANCE = 100000;
    private static final int BAD_INIT_BALANCE = 10000;
    private static final String FULL_NAME = "Thomas Anderson";
    private static final String PHONE = "(555) 567-8900";
    private static final String EMAIL = "neo@metacortex.com";
    private static final String STREET = "101 Hackett Street";
    private static final String CITY = "Mega City";
    private static final String STATE = "IL";
    private static final String ZIP = "60666";
    private static final String ISSUER = "Commonwealth Bank";
    private static final String CARD_TYPE = "MasterChip";
    private static final String HOLDER = "Thomas A Anderson";
    private static final String ACCT_NO = "1234-5678-9012-3456";
    private static final String EXPIRATION_DATE = "03/05";
    private AccountFactory accountFactory;
    private ClassPathXmlApplicationContext appContext;

    public accountTest() {
    }

    @Before
    public void setUp() throws Exception {
        this.appContext = new ClassPathXmlApplicationContext("context.xml");
        this.accountFactory = (AccountFactory)this.appContext.getBean("AccountFactory", AccountFactory.class);
    }

    @After
    public void tearDown() {
        if (this.appContext != null) {
            this.appContext.close();
        }

    }

    @Test
    public void testGoodAccountCreation() throws Exception {
        Account acct = this.accountFactory.newAccount("neotheone", PASSWORD_BYTES, 100000);
        acct.setFullName("Thomas Anderson");
        acct.setPhone("(555) 567-8900");
        acct.setEmail("neo@metacortex.com");
        Address addr = (Address)this.appContext.getBean("Address", Address.class);
        addr.setStreetAddress("101 Hackett Street");
        addr.setCity("Mega City");
        addr.setState("IL");
        addr.setZipCode("60666");
        acct.setAddress(addr);
        CreditCard card = (CreditCard)this.appContext.getBean("CreditCard", CreditCard.class);
        card.setType("MasterChip");
        card.setIssuer("Commonwealth Bank");
        card.setHolder("Thomas A Anderson");
        card.setAccountNumber("1234-5678-9012-3456");
        card.setExpirationDate("03/05");
        acct.setCreditCard(card);
        Assert.assertEquals("neotheone", acct.getName());
        Assert.assertEquals(100000L, (long)acct.getBalance());
        Assert.assertEquals("Thomas Anderson", acct.getFullName());
        Assert.assertEquals("(555) 567-8900", acct.getPhone());
        Assert.assertEquals("neo@metacortex.com", acct.getEmail());
        Address verifyAddr = acct.getAddress();
        Assert.assertEquals("101 Hackett Street", verifyAddr.getStreetAddress());
        Assert.assertEquals("Mega City", verifyAddr.getCity());
        Assert.assertEquals("IL", verifyAddr.getState());
        Assert.assertEquals("60666", verifyAddr.getZipCode());
        Assert.assertNotNull(verifyAddr.toString());
        CreditCard verifyCard = acct.getCreditCard();
        Assert.assertEquals("MasterChip", verifyCard.getType());
        Assert.assertEquals("Commonwealth Bank", verifyCard.getIssuer());
        Assert.assertEquals("Thomas A Anderson", verifyCard.getHolder());
        Assert.assertEquals("1234-5678-9012-3456", verifyCard.getAccountNumber());
        Assert.assertEquals("03/05", verifyCard.getExpirationDate());
        acct.setBalance(10000);
        Assert.assertEquals(10000L, (long)acct.getBalance());
        Assert.assertTrue(Arrays.equals(PASSWORD_BYTES, acct.getPasswordHash()));
        acct.setPasswordHash(PASSWORD_BYTES_UPDATED);
        Assert.assertTrue(Arrays.equals(PASSWORD_BYTES_UPDATED, acct.getPasswordHash()));
    }

    @Test
    public void testBadNameAccountCreation() throws Exception {
        Assert.assertNull(this.accountFactory.newAccount("theone", PASSWORD_BYTES, 100000));
    }

    @Test
    public void testSetAccountName() throws Exception {
        Account acct = this.accountFactory.newAccount("neotheone", PASSWORD_BYTES, 100000);

        try {
            acct.setName("theone");
            Assert.fail("Shouldn't be able to set the name to 'theone'");
        } catch (AccountException var3) {
            acct.setName("tanderson");
        }

    }

    @Test
    public void testBadBalanceAccountCreation() throws Exception {
        Assert.assertNull(this.accountFactory.newAccount("neotheone", PASSWORD_BYTES, 10000));
    }
}

