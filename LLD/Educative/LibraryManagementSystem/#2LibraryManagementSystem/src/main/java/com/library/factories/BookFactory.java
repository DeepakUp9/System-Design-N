package com.library.factories;

import com.library.enums.BookFormat;
import com.library.models.Author;
import com.library.models.Book;
import com.library.models.BookItem;
import com.library.models.Rack;

import java.time.LocalDate;
import java.util.List;

/**
 * Factory for creating Book and BookItem objects.
 * 
 * DESIGN PATTERN: Factory Pattern
 * - Centralizes book creation logic
 * - Ensures books are created with valid data
 * - Simplifies client code
 * 
 * SOLID PRINCIPLE: Open/Closed Principle
 * - Can extend factory to create new types of library materials
 * - Existing code doesn't need modification
 */
public class BookFactory {
    
    /**
     * Create a Book with essential information.
     * Requirement R3: Book includes ISBN, title, author, subject, publication date.
     * 
     * @param ISBN Book's ISBN
     * @param title Book's title
     * @param authors List of authors
     * @param subject Book's subject
     * @return New Book instance
     */
    public static Book createBook(String ISBN, String title, List<Author> authors, String subject) {
        validateBookInput(ISBN, title, authors, subject);
        return new Book(ISBN, title, authors, subject);
    }
    
    /**
     * Create a Book with complete information.
     * 
     * @param ISBN Book's ISBN
     * @param title Book's title
     * @param authors List of authors
     * @param subject Book's subject
     * @param publisher Book's publisher
     * @param publicationDate Book's publication date
     * @param numberOfPages Number of pages
     * @param language Book's language
     * @param format Book format
     * @return New Book instance with all metadata
     */
    public static Book createDetailedBook(String ISBN, String title, List<Author> authors, 
                                         String subject, String publisher, LocalDate publicationDate,
                                         int numberOfPages, String language, BookFormat format) {
        Book book = createBook(ISBN, title, authors, subject);
        book.setPublisher(publisher);
        book.setPublicationDate(publicationDate);
        book.setNumberOfPages(numberOfPages);
        book.setLanguage(language != null ? language : "English");
        book.setFormat(format != null ? format : BookFormat.HARDCOVER);
        return book;
    }
    
    /**
     * Create a BookItem (physical copy) for a book.
     * Requirement R4: Multiple copies per book with unique IDs.
     * 
     * @param book The book this item belongs to
     * @param rack The rack where this item is stored
     * @param price The price of this copy
     * @return New BookItem instance
     */
    public static BookItem createBookItem(Book book, Rack rack, double price) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        if (rack == null) {
            throw new IllegalArgumentException("Rack cannot be null");
        }
        
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        
        return new BookItem(book, rack, price);
    }
    
    /**
     * Create multiple BookItems for a book.
     * 
     * @param book The book
     * @param rack The rack location
     * @param price Price per copy
     * @param quantity Number of copies to create
     * @return List of BookItem instances
     */
    public static List<BookItem> createMultipleBookItems(Book book, Rack rack, double price, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        return java.util.stream.IntStream.range(0, quantity)
                .mapToObj(i -> createBookItem(book, rack, price))
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Validate book input parameters.
     * 
     * @param ISBN Book's ISBN
     * @param title Book's title
     * @param authors List of authors
     * @param subject Book's subject
     */
    private static void validateBookInput(String ISBN, String title, List<Author> authors, String subject) {
        if (ISBN == null || ISBN.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        
        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Book must have at least one author");
        }
        
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
    }
}
