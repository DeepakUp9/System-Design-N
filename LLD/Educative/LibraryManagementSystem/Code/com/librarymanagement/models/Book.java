package com.librarymanagement.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.librarymanagement.enums.BookFormat;
import com.librarymanagement.enums.BookStatus;

/**
 * Book metadata (R3, R4): ISBN, title, subject, authors, publication date.
 * Class diagram: Composition — Book is composed of BookItem (1 -- 1..*); each copy is a BookItem.
 * Encapsulation: private final fields; copies list mutable only via addCopy().
 * SOLID: SRP — single responsibility of holding book metadata and its copies.
 */
public class Book {
    private final String isbn;
    private final String title;
    private final String subject;
    private final String publisher;
    private final Date publicationDate;
    private final String language;
    private final int numberOfPages;
    private final BookFormat format;
    private final List<Author> authors;
    private final List<BookItem> copies;

    public Book(String isbn, String title, String subject, String publisher,
                Date publicationDate, String language, int numberOfPages,
                BookFormat format, List<Author> authors) {
        this.isbn = isbn;
        this.title = title;
        this.subject = subject;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.language = language;
        this.numberOfPages = numberOfPages;
        this.format = format;
        this.authors = authors == null ? new ArrayList<>() : new ArrayList<>(authors);
        this.copies = new ArrayList<>();
    }

    /** Class diagram: Book composed of BookItem; add a physical copy. */
    public void addCopy(BookItem item) {
        if (item != null && item.getBook() == this) {
            copies.add(item);
        }
    }

    public List<BookItem> getCopies() {
        return Collections.unmodifiableList(copies);
    }

    /** Return first available copy for issuing. */
    public BookItem getAvailableCopy() {
        for (BookItem c : copies) {
            if (c.getStatus() == BookStatus.AVAILABLE || c.getStatus() == BookStatus.RESERVED) {
                return c;
            }
        }
        return null;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getSubject() { return subject; }
    public String getPublisher() { return publisher; }
    public Date getPublicationDate() { return publicationDate; }
    public String getLanguage() { return language; }
    public int getNumberOfPages() { return numberOfPages; }
    public BookFormat getFormat() { return format; }
    public List<Author> getAuthors() { return Collections.unmodifiableList(authors); }
}
