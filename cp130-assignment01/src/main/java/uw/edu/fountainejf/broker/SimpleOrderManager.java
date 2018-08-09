package uw.edu.fountainejf.broker;

import java.util.Comparator;
import java.util.function.Consumer;

import edu.uw.ext.framework.broker.OrderManager;
import edu.uw.ext.framework.broker.OrderQueue;
import edu.uw.ext.framework.order.StopBuyOrder;
import edu.uw.ext.framework.order.StopSellOrder;

public final class SimpleOrderManager implements OrderManager{

    /** Symbol of the stock */
    private String stockTickerSymbol;

    /** Queue for stop buy orders*/
    private OrderQueue<Integer,StopBuyOrder> stopBuyOrderQueue;

    /** Queue for stop sell orders*/
    private OrderQueue<Integer,StopSellOrder> stopSellOrderQueue;

    public SimpleOrderManager(final String stockTickerSymbol,final int price){
        this.stockTickerSymbol = stockTickerSymbol;
        // Create the stop buy order queue and associated pieces

        stopBuyOrderQueue = new SimpleOrderQueue<>(stockTickerSymbol, price,
                (t,o) -> o.getPrice()<= t,
                Comparator.comparing(StopBuyOrder::getPrice)
                        .thenComparing(StopBuyOrder::compareTo));


        stopSellOrderQueue = new SimpleOrderQueue<>(stockTickerSymbol, price,
                (t,o) -> o.getPrice()>= t,
                Comparator.comparing(StopSellOrder::getPrice).reversed()
                        .thenComparing(StopSellOrder::compareTo));
    }

    public void adjustPrice(int price) {
        stopBuyOrderQueue.setThreshold(price);
        stopSellOrderQueue.setThreshold(price);
    }


    public final void queueOrder(StopBuyOrder order){

        stopBuyOrderQueue.enqueue(order);
    }

    public final void queueOrder(StopSellOrder order){

        stopSellOrderQueue.enqueue(order);
    }

    public final void setBuyOrderProcessor(final Consumer<StopBuyOrder> processor){
        stopBuyOrderQueue.setOrderProcessor(processor);
    }


    public final void setSellOrderProcessor(final Consumer<StopSellOrder> processor){
        stopSellOrderQueue.setOrderProcessor(processor);
    }


    public final String getSymbol(){

        return stockTickerSymbol;
    }


}