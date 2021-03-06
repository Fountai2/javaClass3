package uw.edu.fountainejf;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountFactory;
import edu.uw.ext.framework.dao.AccountDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * creates directory
 * writes, reads
 */
public class SimpleAccountDao implements AccountDao {
    Logger logger = LoggerFactory.getLogger(SimpleAccount.class);

    private static final String DIRECTORY = "target" + File.separator +"accounts" + File.separator;
    private String NULL = "<null>";

    @Override
    public Account getAccount(String aName) {
        try{
            Account acct = readAccount(aName);
            if(acct != null) {
                acct.setAddress(readAddress(aName));
                acct.setCreditCard(readCreditCard(aName));
            }
            return acct;
        } catch (AccountException e){
            e.printStackTrace();
        }
        return null;
        //if account is null, set account, else set addy and cc
    }


    private SimpleAccount readAccount(String aName) throws AccountException {
        try {
            DataInputStream dataInputStream = new DataInputStream(
                    new FileInputStream(DIRECTORY + aName + "/account"));
            String name = dataInputStream.readUTF();
            int len = dataInputStream.readInt();

            byte[] passHash = new byte[len];
            dataInputStream.readFully(passHash);
            int bal = dataInputStream.readInt();
            String fullName = dataInputStream.readUTF();
            String phone = dataInputStream.readUTF();
            String email = dataInputStream.readUTF();

            SimpleAccount account = new SimpleAccount();
            account.setName(name);
            account.setBalance(bal);
            account.setPasswordHash(passHash);
            if (!fullName.equals(NULL)){
                account.setFullName(fullName);
            }
            if (!phone.equals(NULL)) {
                account.setPhone(phone);
            }
            if (!email.equals(NULL)) {
                account.setEmail(email);
            }
            return account;
        } catch (FileNotFoundException e) {
            logger.info("Account doesnt exist", e);
        } catch (IOException e) {
            logger.warn("File closed before operation could finish", e);
        } catch (AccountException e) {
            logger.warn("Account Name is Invalid", e);
        }
        return null;
    }
    private CreditCard readCreditCard(String aName) {
        try {
            DataInputStream dataInputStream = new DataInputStream(
                    new FileInputStream(DIRECTORY + aName + "/creditcard"));
            // Avenger Bank Virginia Potts 7890-1234-5678-9000 04/06 <null>
            String issuer = dataInputStream.readUTF();
            String holder = dataInputStream.readUTF();
            String accountNumber = dataInputStream.readUTF();
            String expyDate = dataInputStream.readUTF();
            String type = dataInputStream.readUTF();

            CreditCard cc = new CreditCard();
            cc.setIssuer(issuer);
            cc.setHolder(holder);
            cc.setAccountNumber(accountNumber);
            cc.setExpirationDate(expyDate);
            if (!type.equals(NULL)){
                cc.setType(type);
            }
            return cc;
        } catch (FileNotFoundException e) {
            logger.info("File doesnt exist", e);
        } catch (IOException e) {
            logger.warn("File closed before operation could finish", e);
        }
        return null;
    }
    private Address readAddress(String aName) {
        try {
            DataInputStream dataInputStream = new DataInputStream(
                    new FileInputStream(DIRECTORY + aName + "/address"));
            // 101 Hackett Street 	Mega City IL 60666
            String street = dataInputStream.readUTF();
            String city = dataInputStream.readUTF();
            String state = dataInputStream.readUTF();
            String zip = dataInputStream.readUTF();

            Address addy = new Address();
            addy.setStreetAddress(street);
            addy.setCity(city);
            addy.setState(state);
            addy.setZipCode(zip);
            return addy;
        } catch (FileNotFoundException e) {
            logger.info("File doesnt exist", e);
        } catch (IOException e) {
            logger.warn("File closed before operation could finish", e);
        }
        return null;
    }



    @Override
    public void setAccount(Account account) throws AccountException {
        String accountDirectory = DIRECTORY + account.getName();
        new File(DIRECTORY + account.getName()).mkdirs();
        writeAccount(account, accountDirectory);
        writeAddress(account, accountDirectory);
        writeCreditCard(account, accountDirectory);
    }

    private void writeAccount(Account account, String aDirectory) throws AccountException {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(aDirectory + "/account"));
            out.writeUTF(account.getName());
            out.writeInt(account.getPasswordHash().length);
            out.write(account.getPasswordHash());
            out.writeInt(account.getBalance());
            if (account.getFullName() == null) {
                out.writeUTF(NULL);
            } else {
                out.writeUTF(account.getFullName());
            }
            if (account.getPhone() == null) {
                out.writeUTF(NULL);
            } else {
                out.writeUTF(account.getPhone());
            }
            if (account.getEmail() == null) {
                out.writeUTF(NULL);
            } else {
                out.writeUTF(account.getEmail());
            }
        } catch (FileNotFoundException e) {
            logger.warn("File does not exist", e);
            throw new AccountException("Account Does not Exist");

        } catch (IOException e) {
            logger.warn("File closed before we could finish", e);
        }
    }
    private void writeAddress(Account account, String aDirectory) throws AccountException {
//        if(account.getAddress()!=null) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(aDirectory + "/address"));
            if (account.getAddress() == null) {
                out.writeUTF(NULL);
            } else {
                if (account.getAddress().getStreetAddress() == null) {
                    out.writeUTF(NULL);
                } else {
                    out.writeUTF(account.getAddress().getStreetAddress());
                }
                if (account.getAddress().getCity() == null) {
                    out.writeUTF(NULL);
                } else {
                    out.writeUTF(account.getAddress().getCity());
                }
                if (account.getAddress().getState() == null) {
                    out.writeUTF(NULL);
                } else {
                    out.writeUTF(account.getAddress().getState());
                }
                if (account.getAddress().getZipCode() == null) {
                    out.writeUTF(NULL);
                } else {
                    out.writeUTF(account.getAddress().getZipCode());
                }
            }
            } catch(FileNotFoundException e){
                logger.warn("File does not exist", e);
                throw new AccountException("Account Does not Exist");

            } catch(IOException e){
                logger.warn("File closed before we could finish", e);
            }

        }

    private void writeCreditCard(Account account, String aDirectory) throws AccountException {
            try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(aDirectory + "/creditcard"));
                if (account.getCreditCard() == null)
                {
                    out.writeUTF(NULL);
                } else {
                out.writeUTF(account.getCreditCard().getIssuer());
                out.writeUTF(account.getCreditCard().getHolder());
                out.writeUTF(account.getCreditCard().getAccountNumber());
                out.writeUTF(account.getCreditCard().getExpirationDate());
                if (account.getCreditCard().getType() == null) {
                    out.writeUTF(NULL);
                } else {
                    out.writeUTF(account.getCreditCard().getType());
                }}
            } catch(FileNotFoundException e){
                logger.warn("File does not exist", e);
                throw new AccountException("Account Does not Exist");

            } catch(IOException e){
                logger.warn("File closed before we could finish", e);
            }
    }


    @Override
    public void deleteAccount(String s) throws AccountException {
        deleteFile(new File(DIRECTORY, s));

    }

    @Override
    public void reset() throws AccountException {
        deleteFile(new File(DIRECTORY));
    }

    @Override
    public void close() throws AccountException {


    }


//    private static void writeString(final DataOutputStream out, String s) throws IOException{
//        out.writeUTF(s == null ? NULL : s);
//    }

    private static void writeByteArray(DataOutputStream out, byte[] b) throws IOException{
//        final int len = (b==null);
    }

    private static byte[] readByteArray(DataInputStream in) throws IOException{
        byte[] bytes = null;
        final int len=in.readInt();

        if (len >=0) {
            bytes = new byte[len];
            in.readFully(bytes);
        }
        return bytes;
    }

    public static Account read(final InputStream in) throws AccountException{
//        final DataInputStream din = new DataInputStream();
//        Account account = new SimpleAccount();
//        try {
//            account.setName(din.readUTF());
//            account.setPasswordHash(readByteArray(din));
//            account.setBalance(din.readInt());
//            account.setFullName(readString(din));
//            account.setPhone(readString(din));
//            account.setEmail(readString(din));
//            return account;
//        }catch (IOException e){
//            throw new AccountException("Cannot read account data", e);
//        }
        return null;
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
}
