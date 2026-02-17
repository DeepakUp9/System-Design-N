package com.library.services;

import com.library.interfaces.SearchStrategy;
import com.library.models.Author;
import com.library.models.Book;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete Strategy for searching books by author name.
 * 
 * DESIGN PATTERN: Strategy Pattern - Concrete Strategy
 * - Implements specific search algorithm for author search
 * - Searches through all authors of a book
 */
public class SearchByAuthorStrategy implements SearchStrategy {
    
    @Override
    public List<Book> search(List<Book> catalog, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return List.of();
        }
        
        String lowerSearchTerm = searchTerm.toLowerCase().trim();
        
        return catalog.stream()
                .filter(book -> book.getAuthors().stream()
                        .anyMatch(author -> author.getName() != null && 
                                          author.getName().toLowerCase().contains(lowerSearchTerm)))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "AUTHOR";
    }
}
