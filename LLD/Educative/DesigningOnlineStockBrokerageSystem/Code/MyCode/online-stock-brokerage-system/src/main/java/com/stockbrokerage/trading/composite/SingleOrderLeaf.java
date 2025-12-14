package com.stockbrokerage.trading.composite;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.service.OrderExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Composite Leaf: Represents a single, atomic trading order.
 */
@Component
@RequiredArgsConstructor
public class SingleOrderLeaf implements TradeableComponent {

    private final Order order;
    private final OrderExecutionService executionService; // Dependency on the Strategy Context

    @Override
    public List<Order> executeTradingAction() {
        // Delegates to the Strategy/State Context (OrderExecutionService)
        Order executedOrder = executionService.processOrder(order);
        System.out.printf("[COMPOSITE LEAF] Executed Single Order: %s. Status: %s%n",
                order.getOrderReferenceId(), executedOrder.getStatus());
        return Collections.singletonList(executedOrder);
    }

    @Override
    public BigDecimal calculateTotalValue() {
        return order.getQuantity().multiply(order.getCurrentPrice());
    }

    // These methods are empty for a Leaf but required by the Component interface
    @Override
    public void add(TradeableComponent component) { /* Not applicable */ }
    @Override
    public void remove(TradeableComponent component) { /* Not applicable */ }
}