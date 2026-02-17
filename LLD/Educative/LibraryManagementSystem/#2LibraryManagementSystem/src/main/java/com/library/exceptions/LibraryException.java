package com.library.exceptions;

/**
 * Base exception class for all library-related exceptions.
 */
public class LibraryException extends RuntimeException {
    public LibraryException(String message) {
        super(message);
    }

    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
