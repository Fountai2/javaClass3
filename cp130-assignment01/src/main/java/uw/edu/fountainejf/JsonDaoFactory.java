package uw.edu.fountainejf;

import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactory;
import edu.uw.ext.framework.dao.DaoFactoryException;

public class JsonDaoFactory implements DaoFactory {

    public AccountDao getAccountDao() throws DaoFactoryException {
        try {
            return new JsonDao();
        } catch (AccountException e){
            throw new DaoFactoryException("Failed to initialize json dao", e);
        }
    }
}
