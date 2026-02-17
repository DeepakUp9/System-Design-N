package com.library.interfaces;

import com.library.models.Book;

import java.util.List;

/**
 * Strategy interface for book search operations.
 * Requirement R14: Search by title, author, subject, or publication date.
 * 
 * DESIGN PATTERN: Strategy Pattern
 * - Defines a family of search algorithms
 * - Makes them interchangeable
 * - Allows search algorithm to vary independently from clients that use it
 * 
 * SOLID PRINCIPLE: Interface Segregation Principle
 * - Focused interface with single responsibility
 * - Clients depend only on methods they use
 */
public interface SearchStrategy {
    /**
     * Search for books based on criteria.
     * 
     * @param catalog List of all books in catalog
     * @param searchTerm The search term
     * @return List of books matching the criteria
     */
    List<Book> search(List<Book> catalog, String searchTerm);
    
    /**
     * Get the search strategy name.
     * 
     * @return Strategy name (e.g., "TITLE", "AUTHOR")
     */
    String getStrategyName();
}
