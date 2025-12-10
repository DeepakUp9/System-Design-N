package com.amazon.catalogservice.domain.exception;

// A standard checked exception for domain logic errors
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}