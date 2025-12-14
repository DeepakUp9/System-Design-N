package com.stockbrokerage.order.state;

import com.stockbrokerage.order.model.Order;

/**
 * State Interface: Defines the contract for behavior specific to a particular order status.
 * All concrete state classes must implement these methods.
 */
public interface OrderState {

    /**
     * Handles initial order checks (e.g., account balance, symbol existence).
     * @param order The order context.
     * @return The next OrderState instance.
     */
    OrderState validate(Order order);

    /**
     * Handles the attempt to execute or route the order to the exchange.
     * @param order The order context.
     * @return The next OrderState instance (e.g., ExecutedState or PendingExecutionState).
     */
    OrderState process(Order order);

    /**
     * Handles the attempt to cancel the order.
     * @param order The order context.
     * @return The next OrderState instance (usually CancelledState, unless already terminal).
     */
    OrderState cancel(Order order);
}