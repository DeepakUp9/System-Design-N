package com.rms.core.context;

import com.rms.core.strategy.FulfillmentStrategy;

/**
 * The Strategy Context: Holds a reference to a Concrete Strategy object
 * and delegates the execution to it.
 */
public class OrderFulfillmentContext {

    private final FulfillmentStrategy strategy;

    // The Spring Framework will inject the correct strategy at runtime via a Factory/Map
    public OrderFulfillmentContext(FulfillmentStrategy strategy) {
        this.strategy = strategy;
    }

    // Delegates the work to the configured strategy
    public void fulfillOrder(OrderContext orderContext) {
        System.out.println("Executing fulfillment strategy for: " + strategy.getChannelType());
        strategy.executeFulfillment(orderContext);
    }
}