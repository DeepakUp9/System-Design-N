package com.amazon.orderservice.domain.state;

import com.amazon.orderservice.domain.model.Order;

// State Interface in the State Pattern
public interface OrderState {

    // Unique name of the state for persistence (e.g., PENDING, SHIPPED)
    String getStatusName();

    // Operations that trigger state transitions or perform actions
    void processPayment(Order order);
    void shipOrder(Order order);
    void cancelOrder(Order order);

    // Helper for resilience/logging
    default void logTransition(Order order, String nextStatus) {
        System.out.println("Order " + order.getId() + " transitioning from " + getStatusName() + " to " + nextStatus);
    }
}