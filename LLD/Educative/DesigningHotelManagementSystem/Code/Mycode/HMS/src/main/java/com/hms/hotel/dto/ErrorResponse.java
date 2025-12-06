package com.hms.hotel.dto;

import java.time.LocalDateTime;

/**
 * Standardized DTO for all API error responses.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {}