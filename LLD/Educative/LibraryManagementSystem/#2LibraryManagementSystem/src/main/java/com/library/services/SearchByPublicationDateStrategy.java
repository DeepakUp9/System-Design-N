package com.library.services;

import com.library.interfaces.SearchStrategy;
import com.library.models.Book;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete Strategy for searching books by publication date.
 * 
 * DESIGN PATTERN: Strategy Pattern - Concrete Strategy
 * - Implements specific search algorithm for publication date search
 * - Supports year-only or full date matching
 */
public class SearchByPublicationDateStrategy implements SearchStrategy {
    
    @Override
    public List<Book> search(List<Book> catalog, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return List.of();
        }
        
        String trimmedTerm = searchTerm.trim();
        
        // Try to parse as full date first
        try {
            LocalDate searchDate = LocalDate.parse(trimmedTerm);
            return catalog.stream()
                    .filter(book -> book.getPublicationDate() != null && 
                                   book.getPublicationDate().equals(searchDate))
                    .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            // If not a full date, try year matching
            try {
                int searchYear = Integer.parseInt(trimmedTerm);
                return catalog.stream()
                        .filter(book -> book.getPublicationDate() != null && 
                                       book.getPublicationDate().getYear() == searchYear)
                        .collect(Collectors.toList());
            } catch (NumberFormatException ex) {
                return List.of();
            }
        }
    }
    
    @Override
    public String getStrategyName() {
        return "PUBLICATION_DATE";
    }
}
