package com.stockbrokerage.order.execution.factory;

import com.stockbrokerage.order.enums.OrderType;
import com.stockbrokerage.order.execution.strategy.OrderExecutionStrategy;
import com.stockbrokerage.order.exceptions.OrderProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory/Locator Pattern: Responsible for providing the correct OrderExecutionStrategy
 * based on the OrderType.
 */
@Service
public class OrderStrategyFactory {

    private final Map<OrderType, OrderExecutionStrategy> strategies = new HashMap<>();

    // Use Spring's ApplicationContext to find all beans implementing the Strategy interface
    @Autowired
    public OrderStrategyFactory(ApplicationContext context) {
        // Collect all beans that implement the OrderExecutionStrategy interface
        Map<String, OrderExecutionStrategy> strategyBeans = context.getBeansOfType(OrderExecutionStrategy.class);

        // This mapping logic requires a convention: e.g., "marketOrderStrategy" -> OrderType.MARKET
        strategyBeans.forEach((beanName, strategy) -> {
            // Convert bean name (e.g., "marketOrderStrategy") to OrderType (e.g., MARKET)
            // A more robust way would be to use a custom annotation on the strategies.

            // Simple logic for this LLD exercise:
            String typeName = beanName.replace("OrderStrategy", "").toUpperCase();
            if (typeName.contains("STOPLOSS")) typeName = "STOP_LOSS"; // Handle specific names
            if (typeName.contains("STOPLIMIT")) typeName = "STOP_LIMIT";

            try {
                OrderType type = OrderType.valueOf(typeName);
                strategies.put(type, strategy);
                System.out.println("Registered Strategy: " + type + " -> " + beanName);
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Could not map bean '" + beanName + "' to a valid OrderType.");
            }
        });

        // Production Check: Ensure all types are covered
        if (strategies.size() != OrderType.values().length) {
            System.err.println("CRITICAL LLD ERROR: Not all OrderType enums have a corresponding strategy registered.");
        }
    }

    /**
     * The Factory Method: Retrieves the specific strategy for the given order type.
     * @param type The OrderType enum.
     * @return The concrete OrderExecutionStrategy implementation.
     */
    public OrderExecutionStrategy getStrategy(OrderType type) {
        OrderExecutionStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new OrderProcessingException("Unsupported Order Type: " + type + ". No execution strategy found.");
        }
        return strategy;
    }
}