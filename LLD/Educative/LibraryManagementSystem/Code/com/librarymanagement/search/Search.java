package com.librarymanagement.search;

import java.util.Date;
import java.util.List;

import com.librarymanagement.models.Book;

/**
 * Search interface (R14): search by title, author, subject, publication date.
 * Class diagram: Catalog implements Search; Aggregation — Catalog contains Book (1 -- 1..*).
 * Design pattern: Strategy. SOLID: ISP, DIP.
 */
public interface Search {
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    List<Book> searchBySubject(String subject);
    List<Book> searchByPublicationDate(Date pubDate);
}
