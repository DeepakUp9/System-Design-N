package com.amazon.orderservice.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OrderRequestDTO(
        @NotNull(message = "User ID is mandatory")
        Long userId,

        @Valid // Enables validation on the contained DTOs
        @NotNull(message = "Shipping information is mandatory")
        ShippingInfoDTO shippingInfo,

        @Valid
        @NotNull(message = "Order must contain at least one item")
        @Size(min = 1, message = "Order must contain at least one item")
        List<OrderItemRequestDTO> items
) {}