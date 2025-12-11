package com.rms.core.strategy;

import com.rms.core.context.OrderContext;

/**
 * The Strategy Interface: Defines the common operation for all concrete strategies.
 * Order fulfillment logic will differ based on the channel (Dine-In, Online, Takeout).
 */
public interface FulfillmentStrategy {

    String getChannelType();
    void executeFulfillment(OrderContext context);
}