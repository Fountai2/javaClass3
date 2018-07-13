package uw.edu.fountainejf;

import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.account.AccountManagerFactory;
import edu.uw.ext.framework.dao.AccountDao;

public class SimpleAccountManagerFactory implements AccountManagerFactory {
    @Override
    public AccountManager newAccountManager(AccountDao accountDao) {
        SimpleAccountManager accountManager = new SimpleAccountManager();
        accountManager.setSimpleAccountDao((SimpleAccountDao)accountDao);
        return accountManager;
    }
}
