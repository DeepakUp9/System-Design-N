package com.amazon.orderservice.domain.state.concrete;

import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.domain.state.OrderState;
import org.springframework.stereotype.Component;

@Component("CANCELLED")
public class CancelledState implements OrderState {

    @Override
    public String getStatusName() {
        return "CANCELLED";
    }

    @Override
    public void processPayment(Order order) {
        // Business Logic: Cannot process payment for cancelled order.
        throw new IllegalStateException("Cannot process payment for CANCELLED order.");
    }

    @Override
    public void shipOrder(Order order) {
        // Business Logic: Cannot ship a cancelled order.
        throw new IllegalStateException("Cannot ship CANCELLED order.");
    }

    @Override
    public void cancelOrder(Order order) {
        // Business Logic: Already cancelled. This is an idempotent operation.
        System.out.println("Order " + order.getId() + " is already CANCELLED.");
    }
}