package com.atm.machine.exception;

import org.springframework.http.HttpStatus;

/**
 * Base custom exception for all ATM-related business logic errors.
 * This allows us to handle all ATM-specific issues uniformly.
 */
public abstract class AtmException extends RuntimeException {

    private final HttpStatus httpStatus;

    public AtmException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}