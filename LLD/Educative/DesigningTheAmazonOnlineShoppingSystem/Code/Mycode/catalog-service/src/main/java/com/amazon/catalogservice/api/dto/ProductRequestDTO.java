package com.amazon.catalogservice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "Product SKU is mandatory")
        String sku,

        @NotBlank(message = "Product name is mandatory")
        String name,

        String description,

        @NotNull(message = "Price is mandatory")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @Positive(message = "Stock quantity must be non-negative")
        int stockQuantity,

        @NotNull(message = "Category ID is mandatory")
        Long parentCategoryId
) {
    // Record simplifies DTO creation and ensures immutability
}