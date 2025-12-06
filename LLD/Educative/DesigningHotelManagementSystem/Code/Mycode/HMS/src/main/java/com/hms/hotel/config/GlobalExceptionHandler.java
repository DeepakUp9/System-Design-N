package com.hms.hotel.config;

import com.hms.hotel.dto.ErrorResponse;
import com.hms.hotel.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles LLD/Business Logic Violations (IllegalStateException)
     * e.g., Trying to check-in an OCCUPIED room (State Pattern) or passing invalid dates (Builder Pattern).
     * Returns HTTP 400 BAD REQUEST.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        log.warn("Business Logic Violation: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Business Rule Violation",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles Resource Not Found (RuntimeException or similar custom exceptions).
     * e.g., Trying to perform an action on a Room that doesn't exist.
     * Returns HTTP 404 NOT FOUND.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(RuntimeException ex, WebRequest request) {
        // NOTE: We rely on descriptive messages in our services (e.g., "Room not found")
        if (ex.getMessage() != null && ex.getMessage().contains("not found")) {
            log.warn("Resource Not Found: {}", ex.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    status.value(),
                    "Resource Not Found",
                    ex.getMessage(),
                    request.getDescription(false)
            );
            return new ResponseEntity<>(error, status);
        }

        // Fallback for general runtime errors
        return handleGenericException(ex, request);
    }

    /**
     * Handles Spring Security Authentication failures.
     * e.g., Invalid credentials, JWT issues.
     * Returns HTTP 401 UNAUTHORIZED.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        log.error("Authentication Failed: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication Failed",
                "Invalid username or password, or token is expired/invalid.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles Persistence Errors (e.g., trying to save a duplicate unique username).
     * Returns HTTP 409 CONFLICT.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        log.error("Data Integrity Violation: {}", ex.getRootCause().getMessage());

        String message = "A resource conflict occurred (e.g., unique constraint violation).";

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Data Conflict",
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles general/unexpected exceptions.
     * Returns HTTP 500 INTERNAL SERVER ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Internal Server Error: ", ex);

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}