package com.amazon.catalogservice.api.mapper;

import com.amazon.catalogservice.api.dto.ProductRequestDTO;
import com.amazon.catalogservice.api.dto.ProductResponseDTO;
import com.amazon.catalogservice.domain.model.composite.ProductItem;

// Utility class to handle conversion between LLD Domain Models and API DTOs
public final class ProductMapper {

    private ProductMapper() {
        // Private constructor to prevent instantiation
    }

    // Maps DTO to Domain Entity for saving
    public static ProductItem toEntity(ProductRequestDTO dto) {
        ProductItem entity = new ProductItem();
        // Assuming setters exist on ProductItem for this mapping
        entity.setSku(dto.sku());
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setStockQuantity(dto.stockQuantity());
        entity.setParentCategoryId(dto.parentCategoryId());
        return entity;
    }

    // Maps Domain Entity to DTO for response
    public static ProductResponseDTO toDTO(ProductItem entity) {
        return new ProductResponseDTO(
                entity.getId(),
                entity.getSku(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStockQuantity(),
                entity.getParentCategoryId()
        );
    }
}