package com.rms.core.composite;

import java.math.BigDecimal;

/**
 * The Component Interface: Defines operations common to both simple items (leaf)
 * and containers (composite).
 */
public interface MenuComponent {

    String getName();
    BigDecimal getPrice();
    void display(); // Simple operation to demonstrate traversal

    // Optional: Composite-specific methods that throw UnsupportedOperationException in Leaf
    default void add(MenuComponent component) {
        throw new UnsupportedOperationException("Operation not supported on this component.");
    }
    default void remove(MenuComponent component) {
        throw new UnsupportedOperationException("Operation not supported on this component.");
    }
}