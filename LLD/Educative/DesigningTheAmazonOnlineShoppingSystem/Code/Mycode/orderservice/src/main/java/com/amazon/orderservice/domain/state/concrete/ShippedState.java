package com.amazon.orderservice.domain.state.concrete;

import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.domain.state.OrderState;
import org.springframework.stereotype.Component;

@Component("SHIPPED")
public class ShippedState implements OrderState {

    @Override
    public String getStatusName() {
        return "SHIPPED";
    }

    @Override
    public void processPayment(Order order) {
        // Business Logic: Payment already completed. This is an idempotent operation.
        System.out.println("Order " + order.getId() + " is already SHIPPED. Payment was completed earlier.");
    }

    @Override
    public void shipOrder(Order order) {
        // Business Logic: Cannot ship an already shipped order.
        throw new IllegalStateException("Cannot ship order in SHIPPED state. Order already shipped.");
    }

    @Override
    public void cancelOrder(Order order) {
        // Business Logic: Cannot cancel an already shipped order.
        throw new IllegalStateException("Cannot cancel order in SHIPPED state. Order already shipped.");
    }
}