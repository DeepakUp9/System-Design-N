package com.stockbrokerage.order.execution.strategy;

import com.stockbrokerage.order.model.Order;

/**
 * Strategy Interface: Defines the contract for executing different types of orders.
 * The core logic is encapsulated here, decoupled from the calling context.
 */
public interface OrderExecutionStrategy {

    /**
     * Executes the specific logic required for this type of order.
     * @param order The order entity to be processed.
     * @return The updated Order object after execution (e.g., status changed to EXECUTED).
     * @throws OrderExecutionException if the execution fails due to market conditions, etc.
     */
    Order execute(Order order);
}