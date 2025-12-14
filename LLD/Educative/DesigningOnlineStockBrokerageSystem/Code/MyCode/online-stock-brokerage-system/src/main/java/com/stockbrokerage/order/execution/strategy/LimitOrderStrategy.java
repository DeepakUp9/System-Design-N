package com.stockbrokerage.order.execution.strategy;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.enums.OrderStatus;
import org.springframework.stereotype.Service;

/**
 * Concrete Strategy 2: Handles the logic for a Limit Order.
 */
@Service("limitOrderStrategy")
public class LimitOrderStrategy implements OrderExecutionStrategy {

    @Override
    public Order execute(Order order) {
        // --- PRODUCTION LOGIC GOES HERE ---

        System.out.println("Validating and routing Limit Order: " + order.getOrderReferenceId() + " at limit price " + order.getLimitPrice());

        // 1. Validate if the current market price meets the limit price (order.getCurrentPrice() <= order.getLimitPrice() for Buy).
        // 2. If condition is NOT met, status remains PENDING_EXECUTION or PENDING_VALIDATION.
        // 3. If condition IS met, proceed to execution (similar to Market Order).

        // Since Limit Orders usually wait, we'll route it to a state where a separate component (a continuous matching engine) monitors it.
        order.setStatus(OrderStatus.PENDING_EXECUTION);

        return order;
    }
}