package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.search;

import java.util.Date;
import java.util.List;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.BookItem;

public interface Search {
    List<BookItem> searchByTitle(String title);
    List<BookItem> searchByAuthor(String author);
    List<BookItem> searchBySubject(String subject);
    List<BookItem> searchByPublicationDate(Date pubDate);
}
