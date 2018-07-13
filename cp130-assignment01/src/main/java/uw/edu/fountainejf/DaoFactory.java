package uw.edu.fountainejf;

import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactoryException;

public class DaoFactory implements edu.uw.ext.framework.dao.DaoFactory {

    @Override
    public AccountDao getAccountDao() throws DaoFactoryException {
            return new SimpleAccountDao();
        }
}
