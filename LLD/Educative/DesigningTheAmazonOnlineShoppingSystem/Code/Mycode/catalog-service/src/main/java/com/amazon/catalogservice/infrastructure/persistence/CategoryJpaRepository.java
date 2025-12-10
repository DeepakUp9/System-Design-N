package com.amazon.catalogservice.infrastructure.persistence;

import com.amazon.catalogservice.domain.model.composite.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    // Finds top-level categories where parentCategoryId is null
    List<Category> findByParentCategoryIdIsNull();
    // Finds subcategories based on the parent ID
    List<Category> findByParentCategoryId(Long parentId);
}