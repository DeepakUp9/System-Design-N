package com.amazon.orderservice.domain.builder;

import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.domain.model.OrderItem;
import com.amazon.orderservice.domain.model.ShippingInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Concrete Builder in the Builder Pattern
public class ConcreteOrderBuilder implements OrderBuilder {

    private final Order order; // The Product object (Order) being built
    private final List<OrderItem> tempItems = new ArrayList<>();

    // Only the Builder can access the Order's private constructor
    public ConcreteOrderBuilder() {
        this.order = Order.createEmpty(); // Assuming a static factory or private constructor on Order
    }

    // Setter methods return 'this' for method chaining (Fluent API)
    @Override
    public OrderBuilder setUserId(Long userId) {
        // Note: The actual Order class would need setters or use reflection for this,
        // but the LLD principle is that the Builder controls the internal state.
        // For simplicity, assume setters are available or use private field access.
        this.order.setUserId(userId);
        return this;
    }

    @Override
    public OrderBuilder addLineItem(OrderItem item) {
        this.tempItems.add(item);
        item.setOrder(this.order); // Link the item back to the order context
        return this;
    }

    @Override
    public OrderBuilder setShippingInfo(ShippingInfo info) {
        this.order.setShippingInfo(info);
        return this;
    }

    @Override
    public OrderBuilder calculateTotal() {
        // Business Logic: This is a critical step ensuring the Order is valid.
        BigDecimal total = tempItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.order.setTotalAmount(total);
        return this;
    }

    @Override
    public Order build() {
        // LLD Validation: Ensure mandatory fields are set before finalizing
        if (order.getUserId() == null || order.getTotalAmount() == null || tempItems.isEmpty()) {
            throw new IllegalStateException("Order must have a user, items, and total calculated before building.");
        }

        // Transfer collected items to the final Order object (assuming a method exists)
        this.order.setItems(this.tempItems);

        return this.order;
    }
}