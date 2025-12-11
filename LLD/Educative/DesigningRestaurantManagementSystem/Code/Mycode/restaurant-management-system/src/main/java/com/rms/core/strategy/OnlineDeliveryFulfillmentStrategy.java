package com.rms.core.strategy;

import com.rms.core.context.OrderContext;
import org.springframework.stereotype.Component;

/**
 * Strategy for online delivery orders.
 */
@Component("onlineDeliveryStrategy") // Spring annotation for DI
public class OnlineDeliveryFulfillmentStrategy implements FulfillmentStrategy {

    @Override
    public String getChannelType() {
        return "ONLINE_DELIVERY";
    }

    @Override
    public void executeFulfillment(OrderContext context) {
        System.out.println("-> Strategy: [ONLINE] Assigning order " + context.getOrderId() +
                " to Dispatch Service and calculating ETA.");
        // Logic: Call external Dispatch Service, confirm driver, send customer notification.
        // The context will then be updated to the next state (e.g., ReadyForDeliveryState).

        // This transition is for demonstration. In the Service layer, we'd persist the change.
        // context.fulfill() will trigger the state change after fulfillment logic.
    }
}