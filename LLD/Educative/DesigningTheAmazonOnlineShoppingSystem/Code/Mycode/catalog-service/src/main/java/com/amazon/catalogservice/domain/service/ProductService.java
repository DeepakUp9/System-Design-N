package com.amazon.catalogservice.domain.service;

import com.amazon.catalogservice.domain.model.composite.ProductItem;
import com.amazon.catalogservice.domain.repository.ProductRepository;
import com.amazon.catalogservice.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// Spring Boot @Service annotation implements the service layer in a clean LLD structure.
@Service
public class ProductService {

    // Dependency on the LLD Repository Interface (ProductRepository), NOT the JPA implementation.
    // This enforces the clean separation of concerns.
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // High-level operation for creating or updating a product
    @Transactional
    public ProductItem createOrUpdateProduct(ProductItem product) {
        // LLD Business Rule: Ensure SKU is unique before saving (if it's a new product)
        if (product.getId() == null && productRepository.findBySku(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("Product with SKU " + product.getSku() + " already exists.");
        }

        // Resilience Check: Add logging before persistence operation
        System.out.println("Attempting to save product: " + product.getName());

        return productRepository.save(product);
    }
    /**
     * LLD Operation: Reduces the stock quantity of a product.
     * Used by the InventoryUpdateListener (Observer).
     */
    @Transactional
    public void reduceInventory(Long productId, Integer quantityToReduce) {
        ProductItem product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found to reduce inventory: " + productId));

        int currentStock = product.getStockQuantity();
        if (currentStock < quantityToReduce) {
            // Edge Case/Resilience: Insufficient stock (handle backorder or notify Order Service)
            throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
        }

        product.setStockQuantity(currentStock - quantityToReduce);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public ProductItem getProductById(Long id) {
        // Production Standard: Use custom exceptions for better API error handling
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProductItem> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ProductItem> getProductsByCategory(Long categoryId) {
        return productRepository.findProductsByCategoryId(categoryId);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Product not found for deletion with ID: " + id);
        }
        productRepository.deleteById(id);
    }
}