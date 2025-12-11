package com.rms.application.exception;

/**
 * Custom Exception for State Pattern Violations.
 * Thrown when an action is invalid for the current OrderState.
 */
public class OrderStateException extends RuntimeException {
    public OrderStateException(String message) {
        super(message);
    }
}