package com.amazon.catalogservice.domain.model.composite;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

// Leaf in Composite Pattern & JPA Entity
@Data
@Entity
@Table(name = "products")
public class ProductItem implements CatalogComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku; // Stock Keeping Unit

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price; // Base price

    private int stockQuantity;

    // The foreign key linking back to its parent Category entity
    private Long parentCategoryId;

    // Constructor, Getters, and Setters Omitted for brevity (assume Lombok or standard boilerplate)

    // LLD Implementation of CatalogComponent methods
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPath() {
        return "/products/" + this.sku;
    }

    @Override
    public BigDecimal calculatePrice() {
        return this.price;
    }

    @Override
    public void displayDetails(int depth) {
        System.out.println(" ".repeat(depth * 2) + "- Product: " + name + " ($" + price + ")");
    }
}