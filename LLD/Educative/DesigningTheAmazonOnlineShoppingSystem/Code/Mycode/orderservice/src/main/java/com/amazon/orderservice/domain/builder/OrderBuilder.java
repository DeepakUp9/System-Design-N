package com.amazon.orderservice.domain.builder;

import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.domain.model.OrderItem;
import com.amazon.orderservice.domain.model.ShippingInfo;

// Builder Interface in the Builder Pattern
public interface OrderBuilder {

    // Steps to build the Order object
    OrderBuilder setUserId(Long userId);
    OrderBuilder addLineItem(OrderItem item);
    OrderBuilder setShippingInfo(ShippingInfo info);
    OrderBuilder calculateTotal(); // Crucial step for calculating final price

    // Finalization step
    Order build();
}