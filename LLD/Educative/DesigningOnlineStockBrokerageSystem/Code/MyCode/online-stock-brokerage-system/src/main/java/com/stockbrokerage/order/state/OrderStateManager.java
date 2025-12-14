package com.stockbrokerage.order.state;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.enums.OrderStatus;
import com.stockbrokerage.order.exceptions.OrderProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Context Component: Manages the current state and delegates calls to the appropriate state object.
 * Uses a State Locator pattern (similar to the Factory) to retrieve the correct state instance.
 */
@Service
public class OrderStateManager {

    // Production-level detail: Map to hold state instances, keyed by OrderStatus
    private final Map<OrderStatus, OrderState> stateMap = new EnumMap<>(OrderStatus.class);

    @Autowired
    public OrderStateManager(Set<OrderState> states) {
        // Spring automatically collects all beans implementing OrderState into the Set.
        for (OrderState state : states) {
            // This requires concrete state classes to be annotated with @Service
            // and have a simple getter for their target status.

            // LLD Trade-off: To avoid circular dependencies and tight coupling,
            // we use a simple casting or interface-based identification here.

            // For production, we'll enforce that concrete states must expose their target status.
            if (state instanceof AbstractOrderState abstractState) {
                stateMap.put(abstractState.getTargetStatus(), abstractState);
                System.out.println("Registered Order State: " + abstractState.getTargetStatus());
            } else {
                System.err.println("Warning: OrderState implementation does not extend AbstractOrderState: " + state.getClass().getSimpleName());
            }
        }
    }

    /**
     * Converts an OrderStatus enum into the corresponding concrete OrderState instance.
     * @param status The OrderStatus enum.
     * @return The concrete OrderState object.
     */
    public OrderState getState(OrderStatus status) {
        OrderState state = stateMap.get(status);
        if (state == null) {
            throw new OrderProcessingException("Order State Machine Error: No state handler found for status " + status);
        }
        return state;
    }
}