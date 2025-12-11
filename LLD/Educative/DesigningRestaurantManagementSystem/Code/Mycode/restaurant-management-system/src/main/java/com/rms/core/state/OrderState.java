package com.rms.core.state;

import com.rms.core.context.OrderContext;

/**
 * The State Interface: Defines the common interface for all concrete states.
 */
public interface OrderState {

    // Core state-specific behaviors
    void processOrder(OrderContext context);
    void fulfillOrder(OrderContext context);
    void completeOrder(OrderContext context);
    void cancelOrder(OrderContext context);
}