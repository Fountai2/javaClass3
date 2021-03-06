package uw.edu.fountainejf;


import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountFactory;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.dao.AccountDao;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimpleAccountManager implements AccountManager {

    private AccountFactory accountFactory;
    private ClassPathXmlApplicationContext appContext;
    private AccountDao dao;
    private String ENCODING = "ISO-8859-1";
    private String ALGORITHM = "SHA-256";


    public SimpleAccountManager(final AccountDao dao) {
        this.dao = dao;
        try {
            accountFactory = new SimpleAccountFactory();
        } catch (final BeansException ex) {
        }
    }

    /**
     * Write the account to the accounts folder
     *
     * @param account
     * @throws AccountException
     */
    @Override
    public void persist(Account account) throws AccountException {
        dao.setAccount(account);
    }

    @Override
    public Account getAccount(String s) throws AccountException {
        Account account = dao.getAccount(s);
        if(account != null) {
            account.registerAccountManager(this);
        }
        return account;
    }

    @Override
    public void deleteAccount(String s) throws AccountException {
        Account account = dao.getAccount(s);
        if (account != null) {
            dao.deleteAccount(account.getName());
        }

    }

    @Override
    public Account createAccount(String s, String s1, int i) throws AccountException {
        SimpleAccount account;
        if (s.length() < 8) {
            throw new AccountException("Name too short");
        } else if (i < 100000) {
            throw new AccountException("Account must start with $1000");
        } else if (dao.getAccount(s) != null) {
            throw new AccountException("Account already exists");
        } else {
            SimpleAccountFactory accountFactory = new SimpleAccountFactory();

            account = (SimpleAccount) accountFactory.newAccount(
                    s,
                    hashPassword(s1),
                    i);
            account.registerAccountManager(this);
            persist(account);
        }
        return account;
    }
    private byte[] hashPassword(String password) throws AccountException {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(password.getBytes(ENCODING));
            return md.digest();
        } catch (final NoSuchAlgorithmException e) {
            throw new AccountException("Unable to find algo", e);
        } catch (UnsupportedEncodingException e) {
            throw new AccountException("Unable to encode", e);
        }
    }

    public boolean validateLogin(final String accountName,
                                              final String password)
            throws AccountException {
        boolean valid = false;
        final Account account = getAccount(accountName);

        if (account != null) {
            final byte[] passwordHash = hashPassword(password);
            valid = MessageDigest.isEqual(account.getPasswordHash(),
                    passwordHash);
        }

        return valid;
    }

    public void setSimpleAccountDao(SimpleAccountDao dao) {
        this.dao = dao;
    }

    @Override
    public void close() throws AccountException {
        dao.close();
        dao = null;

    }
}
