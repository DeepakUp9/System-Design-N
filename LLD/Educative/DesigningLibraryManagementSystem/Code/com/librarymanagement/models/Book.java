package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models;

import java.util.Date;
import java.util.List;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.enums.BookFormat;

public class Book {
    private String isbn;
    private String title;
    private String subject;
    private String publisher;
    private String language;
    private Date publicationDate;
    private int numberOfPages;
    private BookFormat format;
    private List<Author> authors;
    
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
        this.authors = authors;
    }
    
    // Getters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getSubject() { return subject; }
    public String getPublisher() { return publisher; }
    public String getLanguage() { return language; }
    public Date getPublicationDate() { return publicationDate; }
    public int getNumberOfPages() { return numberOfPages; }
    public BookFormat getFormat() { return format; }
    public List<Author> getAuthors() { return authors; }
}
