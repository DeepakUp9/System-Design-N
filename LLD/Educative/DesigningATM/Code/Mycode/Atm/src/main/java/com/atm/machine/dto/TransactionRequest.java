package com.atm.machine.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for all financial transaction requests (Withdrawal/Deposit/Transfer).
 */
@Data
public class TransactionRequest {

    // Identifies the account session established during login
    @NotNull(message = "Account ID is required for the transaction session")
    private Long accountId;

    // Used for validation of the ATM itself (e.g., location, machine ID)
    @NotBlank(message = "ATM identifier is required")
    private String atmIdentifier;

    // The amount must be a positive value, greater than zero for movements
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    // Optional field for transfers (destination account)
    @Pattern(regexp = "^[0-9]{0,20}$", message = "Reference account must be digits and max 20 length")
    private String referenceAccount;

    // STEP 11 ADDITION: OTP for high-risk transactions (e.g., large transfers)
    private String otpCode;
}