package com.amazon.orderservice.infrastructure.messaging.events;

import com.amazon.orderservice.domain.model.Order;
import java.util.List;
import java.util.stream.Collectors;

// The DTO used for asynchronous communication
public record OrderPlacedEvent(
        Long orderId,
        Long userId,
        List<ItemDetail> items
) {
    public OrderPlacedEvent(Order order) {
        this(
                order.getId(),
                order.getUserId(),
                order.getItems().stream()
                        .map(item -> new ItemDetail(item.getProductId(), item.getQuantity()))
                        .collect(Collectors.toList())
        );
    }

    // Nested record for item details
    public record ItemDetail(Long productId, Integer quantity) {}
}