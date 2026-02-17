package com.library.exceptions;

/**
 * Exception thrown when a member has outstanding fines.
 */
public class OutstandingFinesException extends LibraryException {
    public OutstandingFinesException(String message) {
        super(message);
    }
}
