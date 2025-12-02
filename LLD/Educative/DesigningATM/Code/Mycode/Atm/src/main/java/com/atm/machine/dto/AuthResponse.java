package com.atm.machine.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * DTO returned upon successful authentication.
 * This represents the authenticated "session" data the ATM machine needs.
 * We include the current balance, customer name, and account details.
 */
@Data
@Builder
public class AuthResponse {

    // Session Identifiers
    private Long accountId;
    private String accountNumber;

    // Customer Information
    private String customerName;

    // Financial Data
    private String accountType; // SAVINGS or CHECKING
    private String currencyCode; // ADDED: Currency of the account
    private BigDecimal currentBalance;

    // Status flag
    private boolean isAuthenticated;
}