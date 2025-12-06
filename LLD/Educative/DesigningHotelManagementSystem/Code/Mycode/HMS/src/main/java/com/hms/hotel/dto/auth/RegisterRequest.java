package com.hms.hotel.dto.auth;  // Create in dto/auth package

import com.hms.hotel.security.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.processing.Pattern;

/**
 * DTO for user registration requests.
 */
public record RegisterRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3-50 characters")
        @Email(message = "Username should be a valid email")  // Optional: if using email as username
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6-100 characters")
        //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$", message = "Password must contain at least one digit, one lowercase, one uppercase, one special character")
        String password,

        Role role  // Optional: default to GUEST if null

) {
    // Validation happens automatically with @Valid annotation in controller
}