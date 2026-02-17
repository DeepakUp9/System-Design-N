package com.library.exceptions;

/**
 * Exception thrown when renewal limit is reached.
 */
public class RenewalLimitExceededException extends LibraryException {
    public RenewalLimitExceededException(String message) {
        super(message);
    }
}
