package com.amazon.catalogservice.domain.service;

import com.amazon.catalogservice.domain.model.composite.Category;
import com.amazon.catalogservice.domain.model.composite.CatalogComponent;
import com.amazon.catalogservice.domain.model.composite.ProductItem;
import com.amazon.catalogservice.domain.repository.CategoryRepository;
import com.amazon.catalogservice.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> getCatalogTree() {
        // LLD Logic: Build the Composite structure starting from the root
        List<Category> rootCategories = categoryRepository.findRootCategories();

        // Recursively build the full tree
        rootCategories.forEach(this::loadChildren);

        return rootCategories;
    }

    /**
     * Recursive method to load all children (sub-categories and products)
     * and attach them to the current Category object, completing the Composite tree.
     */
    private void loadChildren(Category parent) {
        // 1. Load Subcategories (Children of type Composite)
        List<Category> subCategories = categoryRepository.findChildrenByParentId(parent.getId());

        for (Category subCategory : subCategories) {
            parent.add(subCategory); // Add to the Composite list
            loadChildren(subCategory); // Recurse down the tree
        }

        // 2. Load Products (Children of type Leaf)
        List<ProductItem> products = productRepository.findProductsByCategoryId(parent.getId());
        for (ProductItem product : products) {
            parent.add(product); // Add to the Composite list
        }
    }
}