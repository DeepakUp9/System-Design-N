package com.rms.core.context;

import com.rms.core.model.OrderStatus;
import com.rms.core.state.OrderState;

/**
 * The Context: Holds the state object and delegates state-specific requests to it.
 * This class also provides the mechanism for changing the current state.
 */
public class OrderContext {
    private OrderState currentState;
    private Long orderId;
    private OrderStatus status; // Mirror status for DB persistence

    // Constructor to initialize with the starting state (NEW)
    public OrderContext(Long orderId) {
        this.orderId = orderId;
        // The framework (Spring) will inject the initial state
        // For now, we'll set it to null, and Spring will manage the setup.
        // We will manage the state transition manually in the concrete classes.
    }

    // --- State Management ---
    public void changeState(OrderState newState) {
        System.out.println("Order " + orderId + " transitioning from "
                + (currentState != null ? currentState.getClass().getSimpleName() : "None")
                + " to " + newState.getClass().getSimpleName());
        this.currentState = newState;
        // Optionally update the DTO/Entity status here for persistence
        // The Spring Service layer will handle the actual DB update.
    }

    // --- State-Delegated Behavior ---
    public void process() {
        if (currentState != null) {
            currentState.processOrder(this);
        }
    }

    public void fulfill() {
        if (currentState != null) {
            currentState.fulfillOrder(this);
        }
    }

    public void complete() {
        if (currentState != null) {
            currentState.completeOrder(this);
        }
    }

    // --- Getters and Setters (simplified) ---
    public Long getOrderId() { return orderId; }
    public OrderState getCurrentState() { return currentState; }
    public void setStatus(OrderStatus status) { this.status = status; }
}