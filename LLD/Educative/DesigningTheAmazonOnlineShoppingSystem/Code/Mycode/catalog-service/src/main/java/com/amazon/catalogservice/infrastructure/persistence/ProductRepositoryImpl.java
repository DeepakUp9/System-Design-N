package com.amazon.catalogservice.infrastructure.persistence;

import com.amazon.catalogservice.domain.model.composite.ProductItem;
import com.amazon.catalogservice.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Implements the LLD Repository Interface using the Spring JPA tool
@Repository // Spring DI annotation for the infrastructure layer
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;

    // Spring Boot DI (Dependency Injection) is used here to fulfill the LLD design
    public ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ProductItem save(ProductItem product) {
        return jpaRepository.save(product);
    }

    @Override
    public Optional<ProductItem> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<ProductItem> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<ProductItem> findProductsByCategoryId(Long categoryId) {
        return jpaRepository.findByParentCategoryId(categoryId);
    }

    @Override
    public Optional<ProductItem> findBySku(String sku) {
        return jpaRepository.findBySku(sku);
    }
}