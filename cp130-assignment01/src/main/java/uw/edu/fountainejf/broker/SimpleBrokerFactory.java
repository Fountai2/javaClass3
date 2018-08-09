package uw.edu.fountainejf.broker;

import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerFactory;
import edu.uw.ext.framework.exchange.StockExchange;

public class SimpleBrokerFactory implements BrokerFactory {
    @Override
    public Broker newBroker(String name, AccountManager accountManager, StockExchange stockExchange) {
        SimpleBroker broker = new SimpleBroker(name, accountManager, stockExchange);
        return broker;
    }
}
