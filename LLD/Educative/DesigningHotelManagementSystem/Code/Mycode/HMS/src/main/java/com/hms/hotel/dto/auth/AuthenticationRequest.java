package com.hms.hotel.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for authentication (login) requests.
 */
public record AuthenticationRequest(

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password

) {}

