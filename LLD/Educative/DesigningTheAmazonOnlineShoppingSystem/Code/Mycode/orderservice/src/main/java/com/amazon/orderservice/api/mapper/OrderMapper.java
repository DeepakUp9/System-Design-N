package com.amazon.orderservice.api.mapper;

import com.amazon.orderservice.api.dto.*;
import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.domain.model.OrderItem;
import com.amazon.orderservice.domain.model.ShippingInfo;

import java.util.List;
import java.util.stream.Collectors;

public final class OrderMapper {

    private OrderMapper() {}

    // 1. Mapping DTOs to Domain Entities for creation
    public static List<OrderItem> toOrderItemEntities(List<OrderItemRequestDTO> dtos) {
        return dtos.stream()
                .map(dto -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(dto.productId());
                    item.setQuantity(dto.quantity());
                    item.setPrice(dto.price());
                    item.setProductName("Placeholder Name"); // In production, this is fetched from Catalog Service
                    return item;
                })
                .collect(Collectors.toList());
    }

    public static ShippingInfo toShippingInfoEntity(ShippingInfoDTO dto) {
        ShippingInfo info = new ShippingInfo();
        info.setRecipientName(dto.recipientName());
        info.setStreetAddress(dto.streetAddress());
        info.setCity(dto.city());
        info.setZipCode(dto.zipCode());
        return info;
    }

    // 2. Mapping Domain Entity to DTO for response
    public static OrderResponseDTO toDTO(Order order) {
        // Map OrderItems back to DTOs
        List<OrderItemRequestDTO> itemDTOs = order.getItems().stream()
                .map(item -> new OrderItemRequestDTO(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice()))
                .collect(Collectors.toList());

        // Map ShippingInfo back to DTO
        ShippingInfoDTO shippingDTO = new ShippingInfoDTO(
                order.getShippingInfo().getRecipientName(),
                order.getShippingInfo().getStreetAddress(),
                order.getShippingInfo().getCity(),
                order.getShippingInfo().getZipCode()
        );

        return new OrderResponseDTO(
                order.getId(),
                order.getStatus(), // Status from the State Pattern
                order.getUserId(),
                order.getTotalAmount(),
                order.getOrderDate(),
                itemDTOs,
                shippingDTO
        );
    }
}