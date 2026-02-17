package com.library.models;

import com.library.interfaces.SearchStrategy;
import com.library.services.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Catalog manages the collection of books and provides search functionality.
 * Requirement R14: Allow users to search by multiple criteria.
 * 
 * DESIGN PATTERN: Strategy Pattern - Context
 * - Uses SearchStrategy interface to perform different types of searches
 * - Allows switching search algorithms at runtime
 * 
 * DESIGN PATTERN: Aggregation
 * - Catalog contains Books (weaker relationship than composition)
 * - Books can exist independently of Catalog
 * 
 * SOLID PRINCIPLE: Open/Closed Principle
 * - Open for extension: Can add new search strategies without modifying Catalog
 * - Closed for modification: Core catalog functionality remains stable
 */
public class Catalog {
    private final List<Book> books;
    private final Map<String, SearchStrategy> searchStrategies;
    
    // Indexes for faster searching (optional optimization)
    private final Map<String, List<Book>> titleIndex;
    private final Map<String, List<Book>> authorIndex;
    private final Map<String, List<Book>> subjectIndex;
    
    public Catalog() {
        this.books = new ArrayList<>();
        this.searchStrategies = new HashMap<>();
        this.titleIndex = new HashMap<>();
        this.authorIndex = new HashMap<>();
        this.subjectIndex = new HashMap<>();
        
        // Register default search strategies
        registerSearchStrategy(new SearchByTitleStrategy());
        registerSearchStrategy(new SearchByAuthorStrategy());
        registerSearchStrategy(new SearchBySubjectStrategy());
        registerSearchStrategy(new SearchByPublicationDateStrategy());
    }
    
    /**
     * Register a search strategy.
     * DESIGN PATTERN: Strategy Pattern - allows adding new strategies
     * 
     * @param strategy The search strategy to register
     */
    public void registerSearchStrategy(SearchStrategy strategy) {
        searchStrategies.put(strategy.getStrategyName(), strategy);
    }
    
    /**
     * Add a book to the catalog.
     * 
     * @param book The book to add
     */
    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
            updateIndexes(book);
        }
    }
    
    /**
     * Remove a book from the catalog.
     * 
     * @param book The book to remove
     */
    public void removeBook(Book book) {
        if (books.remove(book)) {
            removeFromIndexes(book);
        }
    }
    
    /**
     * Get all books in the catalog.
     * 
     * @return Unmodifiable list of books
     */
    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(books);
    }
    
    /**
     * Get total number of books in catalog.
     * 
     * @return Book count
     */
    public int getBookCount() {
        return books.size();
    }
    
    /**
     * Find book by ISBN.
     * 
     * @param isbn The ISBN to search for
     * @return The book if found, null otherwise
     */
    public Book findBookByISBN(String isbn) {
        return books.stream()
                .filter(book -> book.getISBN().equals(isbn))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Search books using a specific strategy.
     * DESIGN PATTERN: Strategy Pattern - uses strategy to perform search
     * 
     * @param strategyName The name of the search strategy
     * @param searchTerm The search term
     * @return List of matching books
     */
    public List<Book> search(String strategyName, String searchTerm) {
        SearchStrategy strategy = searchStrategies.get(strategyName.toUpperCase());
        
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown search strategy: " + strategyName);
        }
        
        return strategy.search(books, searchTerm);
    }
    
    /**
     * Search books by title.
     * Requirement R14: Search by title
     * 
     * @param title The title to search for
     * @return List of matching books
     */
    public List<Book> searchByTitle(String title) {
        return search("TITLE", title);
    }
    
    /**
     * Search books by author.
     * Requirement R14: Search by author
     * 
     * @param author The author name to search for
     * @return List of matching books
     */
    public List<Book> searchByAuthor(String author) {
        return search("AUTHOR", author);
    }
    
    /**
     * Search books by subject.
     * Requirement R14: Search by subject
     * 
     * @param subject The subject to search for
     * @return List of matching books
     */
    public List<Book> searchBySubject(String subject) {
        return search("SUBJECT", subject);
    }
    
    /**
     * Search books by publication date.
     * Requirement R14: Search by publication date
     * 
     * @param date The date or year to search for
     * @return List of matching books
     */
    public List<Book> searchByPublicationDate(String date) {
        return search("PUBLICATION_DATE", date);
    }
    
    /**
     * Get all available books (at least one copy available).
     * 
     * @return List of books with available copies
     */
    public List<Book> getAvailableBooks() {
        return books.stream()
                .filter(Book::hasAvailableCopy)
                .collect(Collectors.toList());
    }
    
    /**
     * Update search indexes when a book is added.
     * Optimization for faster searching.
     * 
     * @param book The book to index
     */
    private void updateIndexes(Book book) {
        // Title index
        String titleKey = book.getTitle().toLowerCase();
        titleIndex.computeIfAbsent(titleKey, k -> new ArrayList<>()).add(book);
        
        // Author index
        for (Author author : book.getAuthors()) {
            String authorKey = author.getName().toLowerCase();
            authorIndex.computeIfAbsent(authorKey, k -> new ArrayList<>()).add(book);
        }
        
        // Subject index
        if (book.getSubject() != null) {
            String subjectKey = book.getSubject().toLowerCase();
            subjectIndex.computeIfAbsent(subjectKey, k -> new ArrayList<>()).add(book);
        }
    }
    
    /**
     * Remove book from indexes when it's removed from catalog.
     * 
     * @param book The book to remove from indexes
     */
    private void removeFromIndexes(Book book) {
        // Remove from title index
        String titleKey = book.getTitle().toLowerCase();
        List<Book> titleBooks = titleIndex.get(titleKey);
        if (titleBooks != null) {
            titleBooks.remove(book);
            if (titleBooks.isEmpty()) {
                titleIndex.remove(titleKey);
            }
        }
        
        // Remove from author index
        for (Author author : book.getAuthors()) {
            String authorKey = author.getName().toLowerCase();
            List<Book> authorBooks = authorIndex.get(authorKey);
            if (authorBooks != null) {
                authorBooks.remove(book);
                if (authorBooks.isEmpty()) {
                    authorIndex.remove(authorKey);
                }
            }
        }
        
        // Remove from subject index
        if (book.getSubject() != null) {
            String subjectKey = book.getSubject().toLowerCase();
            List<Book> subjectBooks = subjectIndex.get(subjectKey);
            if (subjectBooks != null) {
                subjectBooks.remove(book);
                if (subjectBooks.isEmpty()) {
                    subjectIndex.remove(subjectKey);
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "Catalog{" +
                "totalBooks=" + books.size() +
                ", availableBooks=" + getAvailableBooks().size() +
                ", searchStrategies=" + searchStrategies.keySet() +
                '}';
    }
}
