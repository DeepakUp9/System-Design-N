package com.stockbrokerage.order.exceptions;

/**
 * Base custom runtime exception for order-related business errors.
 */
public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String message) {
        super(message);
    }
}