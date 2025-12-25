package com.espn.cricinfo.api.dto;

import java.util.List;

/**
 * DTO for JWT authentication response.
 */
public record JwtResponse(
        String accessToken,
        String refreshToken,
        String username,
        String type,
        List<String> roles
) {
    public JwtResponse(String accessToken, String refreshToken, String username, String type) {
        this(accessToken, refreshToken, username, type, List.of());
    }
}
