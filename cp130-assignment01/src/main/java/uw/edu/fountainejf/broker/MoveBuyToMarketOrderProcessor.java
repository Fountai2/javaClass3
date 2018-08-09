package uw.edu.fountainejf.broker;

import java.util.TreeSet;
import java.util.function.Consumer;

public class MoveBuyToMarketOrderProcessor implements Consumer {

    /**
     * Performs this operation on the given argument.
     *
     * @param o the input argument
     */
    @Override
    public void accept(Object o) {

    }

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    @Override
    public Consumer andThen(Consumer after) {
        return null;
    }
}
