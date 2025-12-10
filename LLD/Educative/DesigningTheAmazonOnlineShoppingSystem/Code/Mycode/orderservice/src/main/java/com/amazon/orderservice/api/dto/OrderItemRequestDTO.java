package com.amazon.orderservice.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

// Used within OrderRequestDTO
public record OrderItemRequestDTO(
        @NotNull(message = "Product ID is mandatory")
        Long productId,

        @NotNull(message = "Quantity is mandatory")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,

        // Note: In a real system, price is looked up from Catalog, but
        // we include it here for local calculation in the current scope.
        @NotNull(message = "Price is mandatory")
        BigDecimal price
) {}