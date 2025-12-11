package com.rms.core.state;

import com.rms.core.context.OrderContext;
import com.rms.core.model.OrderStatus;
import org.springframework.stereotype.Component;

/**
 * Concrete State: Order is being prepared (kitchen/bar).
 */
@Component("processingState")
public class ProcessingState implements OrderState {

    @Override
    public void processOrder(OrderContext context) {
        System.out.println("State: [PROCESSING] Order is already being processed.");
    }

    @Override
    public void fulfillOrder(OrderContext context) {
        System.out.println("State: [PROCESSING] Preparation finished. Moving to READY_FOR_SERVICE.");
        context.setStatus(OrderStatus.READY_FOR_SERVICE);

        // Context will be changed to ReadyForServiceState
    }

    @Override
    public void completeOrder(OrderContext context) {
        System.out.println("State: [PROCESSING] Cannot complete yet, still in preparation.");
    }

    @Override
    public void cancelOrder(OrderContext context) {
        System.out.println("State: [PROCESSING] Cancelling order " + context.getOrderId() + ". Halting kitchen production.");
        // Logic: Notify kitchen to stop production, calculate wasted inventory.
        context.setStatus(OrderStatus.CANCELLED);
    }
}