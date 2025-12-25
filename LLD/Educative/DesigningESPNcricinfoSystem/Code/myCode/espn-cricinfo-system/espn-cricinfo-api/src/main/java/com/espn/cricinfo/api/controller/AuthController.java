package com.espn.cricinfo.api.controller;

import com.espn.cricinfo.api.dto.*;
import com.espn.cricinfo.config.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Authentication controller for login, signup, and token management.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and user management endpoints")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    /**
     * User login endpoint.
     */
    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT tokens")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken,
                userDetails.getUsername(), "Bearer"));
    }

    /**
     * User registration endpoint.
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user account")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // TODO: Implement user registration logic
        // This would typically create a new user in the database

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "username", signUpRequest.username(),
                "email", signUpRequest.email()
        ));
    }

    /**
     * Refresh JWT token.
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT access token using refresh token")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {

        String refreshToken = request.refreshToken();

        if (refreshToken != null && jwtUtils.validateJwtToken(refreshToken)) {
            String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
            String tokenType = jwtUtils.getTokenTypeFromJwtToken(refreshToken);

            if ("refresh".equals(tokenType)) {
                // Generate new access token
                String newAccessToken = jwtUtils.generateTokenFromUsername(username);

                return ResponseEntity.ok(new JwtResponse(newAccessToken, refreshToken, username, "Bearer"));
            }
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * Logout endpoint (client-side token invalidation).
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout user (invalidate tokens)")
    public ResponseEntity<Map<String, String>> logoutUser() {
        // In a JWT stateless system, logout is handled client-side
        // Server-side token blacklisting could be implemented if needed

        return ResponseEntity.ok(Map.of(
                "message", "User logged out successfully"
        ));
    }

    /**
     * Get current user profile.
     */
    @GetMapping("/profile")
    @Operation(summary = "Get current user profile information")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: Get user details from database
        Map<String, Object> userProfile = Map.of(
                "username", username,
                "email", username + "@cricinfo.com",
                "roles", authentication.getAuthorities().stream()
                        .map(Object::toString)
                        .toList(),
                "enabled", true
        );

        return ResponseEntity.ok(userProfile);
    }

    /**
     * Change password endpoint.
     */
    @PostMapping("/change-password")
    @Operation(summary = "Change user password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // TODO: Implement password change logic with proper validation
        // Check old password, validate new password, update in database

        return ResponseEntity.ok(Map.of(
                "message", "Password changed successfully",
                "username", username
        ));
    }

    /**
     * Forgot password endpoint.
     */
    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        // TODO: Implement password reset logic
        // Generate reset token, send email, etc.

        return ResponseEntity.ok(Map.of(
                "message", "Password reset email sent",
                "email", request.email()
        ));
    }

    /**
     * Reset password with token.
     */
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password using reset token")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        // TODO: Implement password reset with token validation

        return ResponseEntity.ok(Map.of(
                "message", "Password reset successfully"
        ));
    }
}
