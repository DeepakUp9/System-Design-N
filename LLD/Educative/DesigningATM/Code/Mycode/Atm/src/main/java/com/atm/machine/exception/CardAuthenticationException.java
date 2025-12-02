package com.atm.machine.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for when a card fails validation or authentication (e.g., wrong PIN, blocked card).
 */
public class CardAuthenticationException extends AtmException {

    public CardAuthenticationException(String message) {
        // Typically leads to HTTP 401 Unauthorized or 403 Forbidden
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
