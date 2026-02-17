package com.library.exceptions;

/**
 * Exception thrown when trying to perform an action on a reserved book.
 */
public class BookReservedException extends LibraryException {
    public BookReservedException(String message) {
        super(message);
    }
}
