package com.amazon.orderservice.domain.state.concrete;

import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.domain.state.OrderState;
import org.springframework.stereotype.Component;

@Component("PROCESSING")
public class ProcessingState implements OrderState {

    // ... (omitting standard methods)

    @Override
    public String getStatusName() {
        return "PROCESSING";
    }

    @Override
    public void processPayment(Order order) {
        // Business Logic: Payment already handled. This is an idempotent operation here.
        System.out.println("Order " + order.getId() + " is already paid and PROCESSING.");
    }

    @Override
    public void shipOrder(Order order) {
        logTransition(order, "SHIPPED");
        // Business Logic: Create shipping label, notify fulfillment service, trigger Observer notifications.

        // State Transition: Move to Shipped State
        order.setCurrentState(new ShippedState());
    }

    @Override
    public void cancelOrder(Order order) {
        logTransition(order, "CANCELLED");
        // Business Logic: May require management approval or penalty since preparation has started.
        order.setCurrentState(new CancelledState());
    }
}