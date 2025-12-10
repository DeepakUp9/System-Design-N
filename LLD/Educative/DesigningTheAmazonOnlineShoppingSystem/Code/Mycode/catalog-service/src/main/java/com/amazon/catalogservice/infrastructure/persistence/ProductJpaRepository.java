package com.amazon.catalogservice.infrastructure.persistence;

import com.amazon.catalogservice.domain.model.composite.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// Spring Boot Infrastructure Layer - Technology Specific
public interface ProductJpaRepository extends JpaRepository<ProductItem, Long> {

    // Query derived from method name
    List<ProductItem> findByParentCategoryId(Long parentCategoryId);

    Optional<ProductItem> findBySku(String sku);
}