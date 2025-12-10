package com.amazon.catalogservice.domain.model.composite;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Composite in Composite Pattern & JPA Entity
@Data
@Entity
@Table(name = "categories")
public class Category implements CatalogComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // Category identifier

    @Column(nullable = false)
    private String name;

    // LLD Design Choice: We use a separate list of children to build the hierarchy in memory for display/traversal.
    // In a real microservice, fetching all children like this is usually done via dedicated repository queries.
    @Transient // This field is for LLD/Composite pattern logic, not for JPA persistence in this structure
    private final List<CatalogComponent> children = new ArrayList<>();

    // The foreign key linking back to its parent Category entity (for persistence)
    private Long parentCategoryId;

    // Constructor, Getters, and Setters Omitted

    // LLD Methods to manage the Composite structure
    public void add(CatalogComponent component) {
        children.add(component);
    }

    public void remove(CatalogComponent component) {
        children.remove(component);
    }

    @Override
    public List<CatalogComponent> getChildren() {
        return children;
    }

    // LLD Implementation of CatalogComponent methods
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPath() {
        return "/categories/" + this.code;
    }

    // Traverses the children to calculate the total price of all items in the category (for reporting/analytical view)
    @Override
    public BigDecimal calculatePrice() {
        return children.stream()
                .map(CatalogComponent::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void displayDetails(int depth) {
        String indent = " ".repeat(depth * 2);
        System.out.println(indent + "+ Category: " + name + " (Items: " + children.size() + ")");
        for (CatalogComponent component : children) {
            component.displayDetails(depth + 1);
        }
    }
}