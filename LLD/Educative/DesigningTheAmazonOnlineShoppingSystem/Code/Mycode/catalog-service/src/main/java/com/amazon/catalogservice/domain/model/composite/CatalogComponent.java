package com.amazon.catalogservice.domain.model.composite;

import java.math.BigDecimal;
import java.util.List;

// Component Interface in Composite Pattern
public interface CatalogComponent {

    String getName();

    String getPath(); // Unique identifier path for searching

    // Operation: Returns the total price (for a product, it's its own price; for a category, it's the sum of its children)
    BigDecimal calculatePrice();

    // Optional: Used for displaying the hierarchy
    void displayDetails(int depth);

    // Composite-specific operation (optional, but useful for LLD design)
    default List<CatalogComponent> getChildren() {
        return List.of();
    }
}