package com.espn.cricinfo.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for token refresh request.
 */
public record TokenRefreshRequest(
        @NotBlank(message = "Refresh token is required")
        String refreshToken
) {}
