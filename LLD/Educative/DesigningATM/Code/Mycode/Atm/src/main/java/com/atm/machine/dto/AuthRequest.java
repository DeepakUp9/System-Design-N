package com.atm.machine.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for card insertion and PIN validation (Login).
 * Uses JSR-303 (Jakarta Validation) annotations for strict input checks.
 */
@Data
public class AuthRequest {

    // Must be a 16-digit number string
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Card number must contain only digits")
    private String cardNumber;

    // PIN is typically 4 or 6 digits. We enforce 4-6 digits here.
    @NotBlank(message = "PIN is required")
    @Size(min = 4, max = 6, message = "PIN must be between 4 and 6 digits")
    @Pattern(regexp = "^[0-9]+$", message = "PIN must contain only digits")
    private String pin;

    // ✅ Add this field
    private String ipAddress;

}