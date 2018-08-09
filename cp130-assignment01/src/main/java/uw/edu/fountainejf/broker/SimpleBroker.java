package uw.edu.fountainejf.broker;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerException;
import edu.uw.ext.framework.broker.OrderManager;
import edu.uw.ext.framework.broker.OrderQueue;
import edu.uw.ext.framework.exchange.ExchangeEvent;
import edu.uw.ext.framework.exchange.ExchangeListener;
import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.*;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class SimpleBroker implements Broker , ExchangeListener {

    private String bname;
    private AccountManager acctMgr;
    private OrderQueue<Boolean, Order> mOrder;
    private StockExchange exch;

    /**
     * For my own sanity - hashmap takes generics of key /values
     * Ordermanager has a ticker value and two orders (sell and buy)
     * We create the hash map to associate the given ticker value with an order manager (what contains
     * the actual orders)
     * Otherwise we have to search for a list of order managers that contain tickers.
     * Now we effectively have a list of tickers that already know what ordermanagers / orders to find
     */
    private HashMap<String, OrderManager> orderManagerHashMap;

    // added a ctor  that names the threadstring name
    public SimpleBroker(String brokerName,
                        final AccountManager accountManager,
                        StockExchange exchange) {
        bname = brokerName;
        acctMgr = accountManager;
        exch = exchange;

        //create market order queue
        // Make it threaded, might need to add a method to OrderQueue
        mOrder = new SimpleOrderQueue<>("marketOrder", exch.isOpen(), (t, o) -> t);
        Consumer<Order> csmr = this::executeOrder;
        mOrder.setOrderProcessor(csmr);
        initializeOrdermanagers();
        exchange.addExchangeListener(this);
    }


    private final void initializeOrdermanagers() {
        orderManagerHashMap = new HashMap<>();
        final Consumer<StopBuyOrder> moveBuy2 = (StopBuyOrder order) -> mOrder.enqueue(order);
        final Consumer<StopSellOrder> moveSell2 = (StopSellOrder order) -> mOrder.enqueue(order);
        for (String ticker : exch.getTickers()){
            final int currPrice = exch.getQuote(ticker).getPrice();
            final OrderManager ordermanager = createOrderManager(ticker, currPrice);
            ordermanager.setBuyOrderProcessor(moveBuy2);
            ordermanager.setSellOrderProcessor(moveSell2);
            orderManagerHashMap.put(ticker, ordermanager);
        }
    }

    private OrderManager createOrderManager(String ticker, int initialPrice){
        return new SimpleOrderManager(ticker, initialPrice);
    }

    private void executeOrder(final Order order){
        try{
                int sharePrice = exch.executeTrade(order);
                final Account acct = acctMgr.getAccount(order.getAccountId());
                acct.reflectOrder(order, sharePrice);
            }catch( final AccountException ex){
                ex.printStackTrace();
            }
    }

    public void priceChanged(ExchangeEvent event) {
        OrderManager orderManager;
        orderManager = orderManagerHashMap.get(event.getTicker());
        if (orderManager!=null){
            orderManager.adjustPrice(event.getPrice());
        }
    }

    public void exchangeOpened(ExchangeEvent event){
        mOrder.setThreshold(Boolean.TRUE);
    }

    public void exchangeClosed(ExchangeEvent event){
        mOrder.setThreshold(Boolean.FALSE);
    }

    @Override
    public String getName() {
        return bname;
    }

    @Override
    public Account createAccount(String name, String passHash, int bal) throws BrokerException {
        try {
            return acctMgr.createAccount(name, passHash, bal);
        } catch (AccountException exc){exc.printStackTrace();}
        return null;
    }

    @Override
    public void deleteAccount(String s) throws BrokerException {
        try {
            acctMgr.deleteAccount(s);
        } catch (AccountException e) {
            throw new BrokerException("Failed to delete the account for: " + s, e);
        }
    }

    @Override
    public Account getAccount(String name, String pass) throws BrokerException {
        try {
            if (acctMgr.validateLogin(name, pass)) {
                return acctMgr.getAccount(name);
            } else {
                throw new BrokerException("Bad creds");
            }
        } catch (AccountException e){
            throw new BrokerException("Cannot get account " + name);
        }
    }

    @Override
    public void placeOrder(MarketBuyOrder marketBuyOrder) throws BrokerException {
        mOrder.enqueue(marketBuyOrder);
    }

    @Override
    public void placeOrder(MarketSellOrder marketSellOrder) throws BrokerException {
        mOrder.enqueue(marketSellOrder);
    }

    @Override
    public void placeOrder(StopBuyOrder stopBuyOrder){
        try {
            getOrderManager(stopBuyOrder.getStockTicker()).queueOrder(stopBuyOrder);
        } catch (BrokerException e){e.printStackTrace();}
    }


    @Override
    public void placeOrder(StopSellOrder stopSellOrder) {
        try {
            getOrderManager(stopSellOrder.getStockTicker()).queueOrder(stopSellOrder);
        } catch (BrokerException e){e.printStackTrace();}
    }

    private final OrderManager getOrderManager(String t) throws BrokerException{
        final OrderManager orderManager = orderManagerHashMap.get(t);
        if (orderManager == null){
            throw new BrokerException("Cannot find ticker " + t);
        }
        return orderManager;
    }

    @Override
    public void close() throws BrokerException {
        try {
            exch.removeExchangeListener(this);
            acctMgr.close();
            orderManagerHashMap = null;
        } catch (AccountException exc) {
            throw new BrokerException(exc);
        }
    }


    public StockQuote requestQuote (final String symbol)
            throws BrokerException{
        final StockQuote quote = exch.getQuote(symbol);
        if(quote == null){
            throw new BrokerException(String.format("Quote not available for '%s'.", symbol));
        }
        return quote;
    }
}
