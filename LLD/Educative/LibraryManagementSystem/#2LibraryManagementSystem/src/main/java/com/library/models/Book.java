package com.library.models;

import com.library.enums.BookFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entity representing the conceptual book metadata.
 * Requirement R3: Book should include ISBN, title, author, subject, publication date.
 * Requirement R4: A book can have multiple copies (BookItems).
 * 
 * DESIGN PATTERN: Composition
 * - Book has a strong composition relationship with BookItem
 * - If a Book is deleted, all its BookItems should be deleted
 */
public class Book {
    private final String ISBN;
    private String title;
    private List<Author> authors;
    private String subject;
    private LocalDate publicationDate;
    private String publisher;
    private int numberOfPages;
    private String language;
    private BookFormat format;
    
    // Composition: Book contains BookItems
    private final List<BookItem> bookItems;

    public Book(String ISBN, String title, List<Author> authors, String subject) {
        validateISBN(ISBN);
        this.ISBN = ISBN;
        this.title = title;
        this.authors = new ArrayList<>(authors);
        this.subject = subject;
        this.bookItems = new ArrayList<>();
        this.format = BookFormat.HARDCOVER;
        this.language = "English";
    }

    private void validateISBN(String ISBN) {
        if (ISBN == null || ISBN.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        String cleaned = ISBN.replaceAll("-", "");
        if (cleaned.length() != 10 && cleaned.length() != 13) {
            throw new IllegalArgumentException("ISBN must be 10 or 13 digits");
        }
    }

    // Getters
    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    public void addAuthor(Author author) {
        if (!authors.contains(author)) {
            authors.add(author);
        }
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public BookFormat getFormat() {
        return format;
    }

    public void setFormat(BookFormat format) {
        this.format = format;
    }

    // BookItem management (Composition)
    public List<BookItem> getBookItems() {
        return Collections.unmodifiableList(bookItems);
    }

    public void addBookItem(BookItem bookItem) {
        if (!bookItems.contains(bookItem)) {
            bookItems.add(bookItem);
        }
    }

    public void removeBookItem(BookItem bookItem) {
        bookItems.remove(bookItem);
    }

    public int getTotalCopies() {
        return bookItems.size();
    }

    public long getAvailableCopiesCount() {
        return bookItems.stream()
                .filter(BookItem::isAvailable)
                .count();
    }

    public BookItem getAvailableCopy() {
        return bookItems.stream()
                .filter(BookItem::isAvailable)
                .findFirst()
                .orElse(null);
    }

    public boolean hasAvailableCopy() {
        return getAvailableCopy() != null;
    }

    public String getAuthorNames() {
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            names.append(authors.get(i).getName());
            if (i < authors.size() - 1) {
                names.append(", ");
            }
        }
        return names.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(ISBN, book.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN);
    }

    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + ISBN + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + getAuthorNames() +
                ", subject='" + subject + '\'' +
                ", availableCopies=" + getAvailableCopiesCount() +
                ", totalCopies=" + getTotalCopies() +
                '}';
    }
}
