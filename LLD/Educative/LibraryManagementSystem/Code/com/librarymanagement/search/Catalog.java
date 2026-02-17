package com.librarymanagement.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.librarymanagement.models.Author;
import com.librarymanagement.models.Book;
import com.librarymanagement.models.BookItem;

/**
 * Class diagram: Catalog implements Search. Aggregation — Catalog contains Book (1 -- 1..*).
 * Search returns List<Book>; books are indexed by title, author, subject, publication date (R14).
 * Thread-safe for add/search.
 */
public class Catalog implements Search {
    private final Map<String, List<Book>> bookTitles;
    private final Map<String, List<Book>> bookAuthors;
    private final Map<String, List<Book>> bookSubjects;
    private final Map<String, List<Book>> bookPublicationDates;
    private final Map<String, Book> booksByIsbn;

    public Catalog() {
        this.bookTitles = new HashMap<>();
        this.bookAuthors = new HashMap<>();
        this.bookSubjects = new HashMap<>();
        this.bookPublicationDates = new HashMap<>();
        this.booksByIsbn = new HashMap<>();
    }

    /** Class diagram: Aggregation — add Book to catalog (1 -- 1..*). */
    public synchronized void addBook(Book book) {
        if (book == null) return;
        booksByIsbn.put(book.getIsbn(), book);
        bookTitles.computeIfAbsent(book.getTitle().toLowerCase(), k -> new ArrayList<>()).add(book);
        for (Author author : book.getAuthors()) {
            bookAuthors.computeIfAbsent(author.getName().toLowerCase(), k -> new ArrayList<>()).add(book);
        }
        bookSubjects.computeIfAbsent(book.getSubject().toLowerCase(), k -> new ArrayList<>()).add(book);
        String pubStr = new SimpleDateFormat("yyyy-MM-dd").format(book.getPublicationDate());
        bookPublicationDates.computeIfAbsent(pubStr, k -> new ArrayList<>()).add(book);
    }

    /** Add BookItem's Book to catalog if not present (used when adding copy to library). */
    public synchronized void addBookIfAbsent(Book book) {
        if (book != null && !booksByIsbn.containsKey(book.getIsbn())) {
            addBook(book);
        }
    }

    @Override
    public List<Book> searchByTitle(String title) {
        if (title == null) return Collections.emptyList();
        return new ArrayList<>(bookTitles.getOrDefault(title.toLowerCase(), Collections.emptyList()));
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        if (author == null) return Collections.emptyList();
        return new ArrayList<>(bookAuthors.getOrDefault(author.toLowerCase(), Collections.emptyList()));
    }

    @Override
    public List<Book> searchBySubject(String subject) {
        if (subject == null) return Collections.emptyList();
        return new ArrayList<>(bookSubjects.getOrDefault(subject.toLowerCase(), Collections.emptyList()));
    }

    @Override
    public List<Book> searchByPublicationDate(Date pubDate) {
        if (pubDate == null) return Collections.emptyList();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(pubDate);
        return new ArrayList<>(bookPublicationDates.getOrDefault(dateStr, Collections.emptyList()));
    }

    public Book getBookByIsbn(String isbn) {
        return booksByIsbn.get(isbn);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(booksByIsbn.values());
    }
}
