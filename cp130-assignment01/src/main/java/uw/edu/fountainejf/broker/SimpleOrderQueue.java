package uw.edu.fountainejf.broker;

import edu.uw.ext.framework.broker.OrderQueue;
import edu.uw.ext.framework.order.Order;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class SimpleOrderQueue<T, E extends Order> implements OrderQueue<T, E>, Runnable  {

    private T threshold;
    private BiPredicate<T, E> filter;
    private Consumer<E> orderProcessor;
    private TreeSet<E> orderList;

    private Thread dispatchedThread;
    final ReentrantLock qLock = new ReentrantLock();
    final ReentrantLock pLock = new ReentrantLock();
    private Condition dCondition = qLock.newCondition();

    public void setPriority(final int priority){
        dispatchedThread.setPriority(priority);
    }

    private void startDispatchThread(final String name){
        dispatchedThread = new Thread(this, name + "-OrderDispatchThread");
        dispatchedThread.setDaemon(true);
        dispatchedThread.start();
    }

    public SimpleOrderQueue(String name, T threshold, BiPredicate<T, E> filter, Comparator<E> cmp){

        orderList = new TreeSet<>(cmp);
        this.threshold = threshold;
        this.filter = filter;
        startDispatchThread(name);
    }

    public SimpleOrderQueue(String name, T threshold, BiPredicate<T, E> filter)
    {
        orderList = new TreeSet<>();
        this.threshold = threshold;
        this.filter = filter;
        startDispatchThread(name);
    }

    @Override
    public void enqueue(E order) {
        qLock.lock();
        try{
            orderList.add(order);
        } finally {
            qLock.unlock();
        }
        dispatchOrders();
    }

    @Override
    public E dequeue() {
        E thisOrder = null;

        qLock.lock();
        try{
        if (!orderList.isEmpty()) {
            thisOrder = orderList.first();

            if (filter.test(threshold, thisOrder)) {
                orderList.remove(thisOrder);
            } else {
                thisOrder = null;
            }
        } } finally {
            qLock.unlock();
        }
        return thisOrder;
    }

    public void dispatchOrders() {
        qLock.lock();

        try{
            dCondition.signal();
        } finally {
            qLock.unlock();
        }
    }

    //allow the order process to be determined by the tests in this case
    public void setOrderProcessor(Consumer<E> consumer) {
        pLock.lock();
        try{
            orderProcessor = consumer;
        } finally {
            pLock.unlock();
        }
    }

    public void setThreshold(final T threshold) {

        this.threshold = threshold;
        dispatchOrders();
    }

    public T getThreshold() {
        return threshold;
    }

    @Override
    public void run() {
        while(true){
            E order;
            qLock.lock();
            try {
                while ((order = dequeue()) == null) {
                    try {
                        dCondition.await();
                    } catch (final InterruptedException e){
                        //log
                    }
                }
            } finally {
                qLock.unlock();
            }

            pLock.lock();
            try {
                if (orderProcessor != null) {
                    orderProcessor.accept(order);
                }
            } finally {
                pLock.unlock();
            }
        }

    }

}
