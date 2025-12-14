package com.stockbrokerage.order.state;

import com.stockbrokerage.order.enums.OrderStatus;
import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.exceptions.OrderTransitionException;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Base class for all concrete state implementations.
 * Handles common functionality like status update and provides the target status for the manager.
 */
public abstract class AbstractOrderState implements OrderState {

    // Abstract property to be defined by subclasses
    @Getter
    protected abstract OrderStatus getTargetStatus();

    /**
     * Production Utility: Logs and updates the order status.
     * @param order The order being transitioned.
     * @param newStatus The target status.
     * @param message Optional message for logging.
     */
    protected void transition(Order order, OrderStatus newStatus, String message) {
        System.out.printf("STATE TRANSITION: %s -> %s for Order %s. Reason: %s%n",
                order.getStatus(), newStatus, order.getOrderReferenceId(), message);
        order.setStatus(newStatus);

        // Additional production-level detail: Store transition history in a separate audit table
        // transactionService.logTransition(order.getId(), order.getStatus(), newStatus, message);
    }

    // Default implementation for terminal states or illegal transitions
    @Override
    public OrderState cancel(Order order) {
        // Default behavior: if not overriden, cancellation is not allowed.
        if (order.getStatus().isTerminal()) { // Assuming we add an isTerminal() method to OrderStatus enum (see update below)
            throw new OrderTransitionException("Cannot cancel an order that is already " + order.getStatus());
        }
        transition(order, OrderStatus.CANCELLED, "Explicit cancellation request.");
        order.setExecutedAt(LocalDateTime.now());
        return OrderStateManager.getCancelledState(); // Assuming a static helper or look up via manager
    }
}