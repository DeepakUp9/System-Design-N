package com.amazon.catalogservice.infrastructure.persistence;

import com.amazon.catalogservice.domain.model.composite.Category;
import com.amazon.catalogservice.domain.repository.CategoryRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;

    public CategoryRepositoryImpl(CategoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // Standard CRUD implementations (omitted for brevity)

    @Override
    public Category save(Category category) {
        return null;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Category> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Category> findRootCategories() {
        return jpaRepository.findByParentCategoryIdIsNull();
    }

    @Override
    public List<Category> findChildrenByParentId(Long parentId) {
        return jpaRepository.findByParentCategoryId(parentId);
    }

    // ... other CRUD methods delegated to jpaRepository
}