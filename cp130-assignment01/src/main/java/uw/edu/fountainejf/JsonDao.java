package uw.edu.fountainejf;
import java.io.*;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.dao.AccountDao;

public class JsonDao implements AccountDao {

    private static final String DIRECTORY = "target" + File.separator +"accounts" + File.separator;
    private String NULL = "<null>";
    private static final byte[] PASSWORD_BYTES = new byte[]{112, 97, 115, 115, 119, 111, 114, 100};
    private String s;

    ObjectMapper mapper = new ObjectMapper();

    public JsonDao() throws AccountException {
        final SimpleModule module = new SimpleModule();
        module.addAbstractTypeMapping(Account.class, SimpleAccount.class);
        module.addAbstractTypeMapping(edu.uw.ext.framework.account.Address.class, Address.class);
        module.addAbstractTypeMapping(edu.uw.ext.framework.account.CreditCard.class, CreditCard.class);
        mapper.registerModule(module);
    }

    public void setAccount(Account account) throws AccountException {
        new File(DIRECTORY).mkdirs();
        File file = new File(DIRECTORY + account.getName() + ".json");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, account);
        } catch (Exception exception) {
        }
    }

    public Account getAccount(String s) {
        File file = new File(DIRECTORY + s +".json");
        //if account exists, and its a dir, then file file = new File(dir, name
        try{
            Account account = mapper.readValue(file, Account.class);
            return account;
        } catch (Exception e){}
        return null;
    }

    @Override
    public void deleteAccount(String s) throws AccountException {
        deleteFile(new File(DIRECTORY + s + ".json"));
    }

    @Override
    public void reset() throws AccountException {
        deleteFile(new File(DIRECTORY));
    }

    public static boolean deleteFile(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteFile(children[i]);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    public void close() throws AccountException {

    }
}