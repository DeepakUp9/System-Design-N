package com.amazon.catalogservice.domain.repository;

import com.amazon.catalogservice.domain.model.composite.Category;
import java.util.List;
import java.util.Optional;

// LLD Repository Interface - Technology Agnostic
public interface CategoryRepository {

    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
    void deleteById(Long id);

    // Business Query for hierarchical structure
    List<Category> findRootCategories();
    List<Category> findChildrenByParentId(Long parentId);
}