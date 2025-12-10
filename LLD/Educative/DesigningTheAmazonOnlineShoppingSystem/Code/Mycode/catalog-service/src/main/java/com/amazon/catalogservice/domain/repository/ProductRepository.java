package com.amazon.catalogservice.domain.repository;

import com.amazon.catalogservice.domain.model.composite.ProductItem;
import java.util.List;
import java.util.Optional;

// LLD Repository Interface - Technology Agnostic
public interface ProductRepository {

    // CRUD Operations
    ProductItem save(ProductItem product);
    Optional<ProductItem> findById(Long id);
    List<ProductItem> findAll();
    void deleteById(Long id);

    // Business Query
    List<ProductItem> findProductsByCategoryId(Long categoryId);
    Optional<ProductItem> findBySku(String sku);
}