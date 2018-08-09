package uw.edu.fountainejf;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountFactory;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args )
    {


        byte[] passwordBytes  = new byte[]{98, 101, 116, 116, 101, 114, 112, 97, 115, 115, 119, 111, 114, 100};

        AccountFactory factory = new SimpleAccountFactory();
        Account account = factory.newAccount("test account", passwordBytes, 100000);
//        System.out.println(account);
//        System.out.println(account.getName() +" ::::: " + account.getBalance() + "   ::::  " + account.getPasswordHash() );
    }
}
