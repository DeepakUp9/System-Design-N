package com.atm.machine.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception handling across all controllers.
 * Catches custom business logic exceptions and validation errors, mapping them
 * to appropriate HTTP status codes and returning a clean JSON error structure.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles custom ATM business logic exceptions (e.g., CardAuthenticationException, ResourceNotFoundException).
     * The HTTP status is derived from the exception itself.
     */
    @ExceptionHandler(AtmException.class)
    public ResponseEntity<Map<String, Object>> handleAtmException(AtmException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", ex.getHttpStatus().value());
        errorDetails.put("error", ex.getHttpStatus().getReasonPhrase());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(errorDetails, ex.getHttpStatus());
    }

    /**
     * Handles Jakarta validation errors (e.g., @NotBlank, @Size constraints failing).
     * Returns HTTP 400 Bad Request.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error.getObjectName(); // Fallback if field name is missing
            if (error.getCodes() != null && error.getCodes().length > 0) {
                // Try to extract the specific field name from the error message
                String[] codes = error.getCodes();
                if (codes.length > 0) {
                    String[] parts = codes[0].split("\\.");
                    fieldName = parts.length > 1 ? parts[parts.length - 1] : fieldName;
                }
            }
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}