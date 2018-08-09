package uw.edu.fountainejf;
import edu.uw.ext.framework.account.*;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class SimpleAccount implements Account {
    Logger logger = LoggerFactory.getLogger(SimpleAccount.class);

    private String name;
    private String fName;
    private String phone;
    private String email;
    private Address addy;
    private int balance;
    private CreditCard cc;
    private byte[] passHash;
    private AccountManager accountManager;

    public void setFullName(String fName){
        logger.info("Setting Full Name to: " + fName );
        this.fName = fName;
    }

    @Override
    public Address getAddress() {
        return addy;
    }

    @Override
    public void setAddress(Address address) {
        addy = address;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String s) {

        phone = s;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String s) {
        email = s;
    }

    @Override
    public CreditCard getCreditCard() {
        return cc;
    }

    @Override
    public void setCreditCard(CreditCard creditCard) {
        cc = creditCard;
    }

    @Override
    public void registerAccountManager(AccountManager accountManager) {
        if(this.accountManager == null) {
            this.accountManager = accountManager;
        } else {
            logger.info("Attempting to set the acct mgr");
        }

    }

    @Override
    public void reflectOrder(Order order, int i) {
        try{
            balance += order.valueOfOrder(i);
            if(accountManager != null){
                accountManager.persist(this);
            } else {
                logger.error("Acct mgr not here", new Exception());
            }
        } catch (final AccountException e){
            e.printStackTrace();
        }

    }

    public String getFullName() {
        return this.fName;
    }

    public void setName(String name) throws AccountException {
        logger.info("Trying to set the account name to " + name);
            if (name.length() >= 8) {
                this.name = name;
            } else throw new AccountException();

    }

    public String getName() {
            return this.name;
        }

    @Override
    public void setBalance(int balanceInCents){
        logger.info("Trying to set the account balance to " + balanceInCents);
            balance = balanceInCents;

    }


    public int getBalance() {
        return this.balance;
    }

    public void setPasswordHash(byte[] passHash){
        byte[] copy = null;
        if (passHash != null){
            copy = new byte[passHash.length];
            System.arraycopy(passHash, 0, copy, 0, passHash.length);
                }
            this.passHash = copy;

    }

    public byte[] getPasswordHash() {
        return this.passHash;
    }

}
