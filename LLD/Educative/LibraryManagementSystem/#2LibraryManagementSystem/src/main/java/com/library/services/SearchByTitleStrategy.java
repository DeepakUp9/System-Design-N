package com.library.services;

import com.library.interfaces.SearchStrategy;
import com.library.models.Book;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete Strategy for searching books by title.
 * 
 * DESIGN PATTERN: Strategy Pattern - Concrete Strategy
 * - Implements specific search algorithm for title search
 * - Case-insensitive partial matching
 */
public class SearchByTitleStrategy implements SearchStrategy {
    
    @Override
    public List<Book> search(List<Book> catalog, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return List.of();
        }
        
        String lowerSearchTerm = searchTerm.toLowerCase().trim();
        
        return catalog.stream()
                .filter(book -> book.getTitle() != null && 
                               book.getTitle().toLowerCase().contains(lowerSearchTerm))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "TITLE";
    }
}
