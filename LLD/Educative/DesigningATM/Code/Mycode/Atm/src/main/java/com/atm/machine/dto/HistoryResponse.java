package com.atm.machine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;;

/**
 * DTO for displaying a single entry in the transaction history.
 */
@Data
@Builder
public class HistoryResponse {
    private Long transactionId;
    private String type; // WITHDRAWAL, DEPOSIT, TRANSFER, BALANCE_INQUIRY
    private String status;
    private BigDecimal amount;
    private ZonedDateTime timestamp;
    private String reference; // e.g., ATM ID or counterparty account number
    private boolean isDebit; // True if money left the account
    private String currencyCode;
}