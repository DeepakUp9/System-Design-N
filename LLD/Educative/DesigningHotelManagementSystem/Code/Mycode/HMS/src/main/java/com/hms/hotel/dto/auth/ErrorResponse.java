package com.hms.hotel.dto.auth;

import java.time.LocalDateTime;

/**
 * DTO for error responses.
 */
public record ErrorResponse(

        String message,
        int status,
        LocalDateTime timestamp,
        String path

) {
    public ErrorResponse(String message, int status, String path) {
        this(message, status, LocalDateTime.now(), path);
    }

    public ErrorResponse(String message) {
        this(message, 400, LocalDateTime.now(), null);
    }
}