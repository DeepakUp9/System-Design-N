package com.amazon.orderservice.domain.state.concrete;

import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.domain.state.OrderState;
import org.springframework.stereotype.Component;

@Component("PENDING") // Use Spring Component for Dependency Injection (DI)
public class PendingState implements OrderState {

    @Override
    public String getStatusName() {
        return "PENDING";
    }

    @Override
    public void processPayment(Order order) {
        logTransition(order, "PROCESSING");
        // Business Logic: Initiate payment gateway request.

        // State Transition: Move to Processing State
        // NOTE: In a real system, the new state object would be fetched from a StateFactory or DI container.
        order.setCurrentState(new ProcessingState());
    }

    @Override
    public void shipOrder(Order order) {
        // Business Logic: Cannot ship before payment is processed.
        throw new IllegalStateException("Cannot ship order in PENDING state. Payment required.");
    }

    @Override
    public void cancelOrder(Order order) {
        logTransition(order, "CANCELLED");
        // State Transition: Move to Cancelled State
        order.setCurrentState(new CancelledState());
        // Business Logic: Release inventory
    }
}