package com.stockbrokerage.trading.composite;

import com.stockbrokerage.order.model.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Composite: Represents a group of tradeable items (e.g., a basket order or portfolio rebalance).
 */
public class BasketOrderComposite implements TradeableComponent {

    private final List<TradeableComponent> components = new ArrayList<>();
    private final String basketId = "BASKET-" + UUID.randomUUID().toString().substring(0, 8);
    private String name;

    public BasketOrderComposite(String name) {
        this.name = name;
        System.out.println("[COMPOSITE] Created Basket Order: " + name + " (" + basketId + ")");
    }

    @Override
    public void add(TradeableComponent component) {
        components.add(component);
    }

    @Override
    public void remove(TradeableComponent component) {
        components.remove(component);
    }

    @Override
    public List<Order> executeTradingAction() {
        List<Order> results = new ArrayList<>();
        System.out.printf("[COMPOSITE EXECUTE] Starting execution for basket '%s' with %d components.%n", name, components.size());

        // Production Logic: Execute all components. Could add rollback logic here.
        for (TradeableComponent component : components) {
            results.addAll(component.executeTradingAction());
        }

        System.out.printf("[COMPOSITE EXECUTE] Basket '%s' execution completed.%n", name);
        return results;
    }

    @Override
    public BigDecimal calculateTotalValue() {
        return components.stream()
                .map(TradeableComponent::calculateTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}