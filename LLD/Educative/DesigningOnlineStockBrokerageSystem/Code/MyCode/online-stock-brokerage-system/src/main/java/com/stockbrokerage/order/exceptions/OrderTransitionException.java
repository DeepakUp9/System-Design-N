package com.stockbrokerage.order.exceptions;

/**
 * Custom exception specifically for illegal state transitions (State Pattern errors).
 */
public class OrderTransitionException extends OrderProcessingException {
    public OrderTransitionException(String message) {
        super(message);
    }
}