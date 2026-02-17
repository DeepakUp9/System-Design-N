package com.librarymanagement.factory;

import java.util.Date;
import java.util.List;

import com.librarymanagement.enums.BookFormat;
import com.librarymanagement.models.Author;
import com.librarymanagement.models.Book;
import com.librarymanagement.models.BookItem;
import com.librarymanagement.models.Rack;

/**
 * Class diagram design pattern: Factory pattern.
 * Centralizes creation of Book and BookItem with validation; prevents inconsistent state.
 * Reference: Class Diagram for the Library Management System.md — "Factory pattern: BookFactory".
 */
public final class BookFactory {

    private BookFactory() {}

    /**
     * Create a Book with validated metadata (R3).
     */
    public static Book createBook(String isbn, String title, String subject, String publisher,
                                  Date publicationDate, String language, int numberOfPages,
                                  BookFormat format, List<Author> authors) {
        if (isbn == null || isbn.isBlank()) throw new IllegalArgumentException("ISBN required");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title required");
        if (publicationDate == null) throw new IllegalArgumentException("Publication date required");
        return new Book(isbn, title, subject, publisher, publicationDate, language, numberOfPages, format, authors);
    }

    /**
     * Create a BookItem (physical copy) and link it to the Book (composition).
     * Class diagram: Book composed of BookItem; each copy has unique ID and Rack.
     */
    public static BookItem createBookItem(String barcode, Book book, Rack rack, double price,
                                          Date dateOfPurchase, Date publicationDate) {
        if (barcode == null || barcode.isBlank()) throw new IllegalArgumentException("Barcode required");
        if (book == null) throw new IllegalArgumentException("Book required");
        if (rack == null) throw new IllegalArgumentException("Rack required");
        BookItem item = new BookItem(barcode, book, rack, price, dateOfPurchase, publicationDate);
        book.addCopy(item);
        return item;
    }
}
