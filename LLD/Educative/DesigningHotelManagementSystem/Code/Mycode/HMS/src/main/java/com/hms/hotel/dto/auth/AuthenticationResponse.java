package com.hms.hotel.dto.auth;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * DTO for authentication responses (contains JWT token).
 */
public record AuthenticationResponse(

        @NotBlank(message = "Token cannot be empty")
        String token,

        String message,  // Optional: success message
        LocalDateTime expiresAt  // Optional: token expiry time

) {
    // Constructor with just token (backward compatibility)
    public AuthenticationResponse(String token) {
        this(token, "Authentication successful", LocalDateTime.now().plusHours(24));
    }

    // Full constructor
    public AuthenticationResponse(String token, String message, LocalDateTime expiresAt) {
        this.token = token;
        this.message = message;
        this.expiresAt = expiresAt;
    }
}