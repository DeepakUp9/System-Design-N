package com.library.exceptions;

/**
 * Exception thrown when trying to borrow an unavailable book.
 */
public class BookNotAvailableException extends LibraryException {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
