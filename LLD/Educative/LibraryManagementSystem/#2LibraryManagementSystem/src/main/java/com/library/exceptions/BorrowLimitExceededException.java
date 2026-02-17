package com.library.exceptions;

/**
 * Exception thrown when a member tries to borrow more books than allowed.
 */
public class BorrowLimitExceededException extends LibraryException {
    public BorrowLimitExceededException(String message) {
        super(message);
    }
}
