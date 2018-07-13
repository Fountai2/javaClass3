package uw.edu.fountainejf;
import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleAccountFactory implements AccountFactory {

    @Override
    public Account newAccount(String s, byte[] bytes, int i) {
        Logger logger = LoggerFactory.getLogger(SimpleAccountFactory.class);
        logger.info("Creating a new account from the factory " + s + i + bytes);

        SimpleAccount thisAccount = new SimpleAccount();
        thisAccount.setBalance(i);

        thisAccount.setPasswordHash(bytes);

        try {
            thisAccount.setName(s);
        } catch (AccountException f){
            f.printStackTrace();
            thisAccount = null;
        }
        if (thisAccount == null || thisAccount.getBalance() < 100000){
            thisAccount = null;}

        return thisAccount;
    }
}
