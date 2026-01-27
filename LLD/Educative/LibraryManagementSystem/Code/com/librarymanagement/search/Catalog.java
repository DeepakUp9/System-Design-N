package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Author;
import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.BookItem;

public class Catalog implements Search {
    private Map<String, List<BookItem>> bookTitles;
    private Map<String, List<BookItem>> bookAuthors;
    private Map<String, List<BookItem>> bookSubjects;
    private Map<String, List<BookItem>> bookPublicationDates;
    private Map<String, BookItem> allBookItems;

    public Catalog() {
        this.bookTitles = new HashMap<>();
        this.bookAuthors = new HashMap<>();
        this.bookSubjects = new HashMap<>();
        this.bookPublicationDates = new HashMap<>();
        this.allBookItems = new HashMap<>();
    }

    public void addBookItem(BookItem bookItem) {
        allBookItems.put(bookItem.getId(), bookItem);
        
        // Index by title
        bookTitles.computeIfAbsent(bookItem.getBook().getTitle(), k -> new ArrayList<>())
                  .add(bookItem);
        
        // Index by author
        for (Author author : bookItem.getBook().getAuthors()) {
            bookAuthors.computeIfAbsent(author.getName(), k -> new ArrayList<>())
                      .add(bookItem);
        }
        
        // Index by subject
        bookSubjects.computeIfAbsent(bookItem.getBook().getSubject(), k -> new ArrayList<>())
                   .add(bookItem);
        
        // Index by publication date
        String pubDateStr = new SimpleDateFormat("yyyy-MM-dd")
                           .format(bookItem.getBook().getPublicationDate());
        bookPublicationDates.computeIfAbsent(pubDateStr, k -> new ArrayList<>())
                           .add(bookItem);
    }

    @Override
    public List<BookItem> searchByTitle(String title) {
        return bookTitles.getOrDefault(title, new ArrayList<>());
    }
    
    @Override
    public List<BookItem> searchByAuthor(String author) {
        return bookAuthors.getOrDefault(author, new ArrayList<>());
    }
    
    @Override
    public List<BookItem> searchBySubject(String subject) {
        return bookSubjects.getOrDefault(subject, new ArrayList<>());
    }
    
    @Override
    public List<BookItem> searchByPublicationDate(Date pubDate) {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(pubDate);
        return bookPublicationDates.getOrDefault(dateStr, new ArrayList<>());
    }
    
    public BookItem getBookItemById(String id) {
        return allBookItems.get(id);
    }
}
