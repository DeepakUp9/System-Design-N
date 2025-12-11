package com.rms.core.state;

import com.rms.core.context.OrderContext;
import com.rms.core.model.OrderStatus;
import org.springframework.stereotype.Component;

/**
 * Concrete State: Order has just been received.
 */
@Component("newOrderState")
public class NewOrderState implements OrderState {

    // Dependency on other states will be managed by Spring DI in the Service layer

    @Override
    public void processOrder(OrderContext context) {
        System.out.println("State: [NEW] Processing order " + context.getOrderId() + ". Moving to PROCESSING.");
        context.setStatus(OrderStatus.PROCESSING);

        // In the Service layer, we will inject the ProcessingState bean
        // and set it here: context.changeState(processingStateBean);
    }

    @Override
    public void fulfillOrder(OrderContext context) {
        System.out.println("State: [NEW] Cannot fulfill a brand new order. Process it first.");
        // Edge Case: Deny action and log.
    }

    @Override
    public void completeOrder(OrderContext context) {
        System.out.println("State: [NEW] Cannot complete a new order.");
    }

    @Override
    public void cancelOrder(OrderContext context) {
        System.out.println("State: [NEW] Cancelling order " + context.getOrderId() + ". Moving to CANCELLED.");
        context.setStatus(OrderStatus.CANCELLED);
        // context.changeState(cancelledStateBean);
    }
}