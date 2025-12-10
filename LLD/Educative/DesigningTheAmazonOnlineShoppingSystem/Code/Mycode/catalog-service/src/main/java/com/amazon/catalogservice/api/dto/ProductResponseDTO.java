package com.amazon.catalogservice.api.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        int stockQuantity,
        Long parentCategoryId
) {
    // Record simplifies DTO creation and ensures immutability
}