package com.stockbrokerage.queue.command;

import com.stockbrokerage.order.model.Order;

/**
 * Command Interface: Defines the contract for an executable operation.
 * Used to encapsulate an order processing request for queuing.
 */
public interface OrderCommand {

    /**
     * Executes the specific command logic.
     */
    void execute();

    /**
     * Production Detail: Used for auditing/logging before execution.
     * @return The target Order entity.
     */
    Order getTargetOrder();
}