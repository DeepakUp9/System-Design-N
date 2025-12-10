package com.amazon.orderservice.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        String status, // Current status, reflecting the State Pattern
        Long userId,
        BigDecimal totalAmount,
        LocalDateTime orderDate,
        List<OrderItemRequestDTO> items,
        ShippingInfoDTO shippingInfo
) {}