package com.rms.core.strategy;

import com.rms.core.context.OrderContext;
import org.springframework.stereotype.Component;

/**
 * Strategy for dine-in orders.
 */
@Component("dineInStrategy")
public class DineInFulfillmentStrategy implements FulfillmentStrategy {

    @Override
    public String getChannelType() {
        return "DINE_IN";
    }

    @Override
    public void executeFulfillment(OrderContext context) {
        System.out.println("-> Strategy: [DINE-IN] Assigning order " + context.getOrderId() +
                " to a specific Table/Waiter.");
        // Logic: Check table availability, update table status, notify waiter.
    }
}