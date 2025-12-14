package com.stockbrokerage.trading.composite;

import com.stockbrokerage.order.model.Order;

import java.math.BigDecimal;
import java.util.List;

/**
 * Composite Component: Defines the interface for all tradeable entities (single orders or groups).
 */
public interface TradeableComponent {

    /**
     * The core execution method, treated uniformly for both Leaf and Composite objects.
     */
    List<Order> executeTradingAction();

    /**
     * Returns the total monetary value involved in this component/group.
     */
    BigDecimal calculateTotalValue();

    // Management methods (only required for the Composite, but defined here for uniformity)
    void add(TradeableComponent component);
    void remove(TradeableComponent component);
}