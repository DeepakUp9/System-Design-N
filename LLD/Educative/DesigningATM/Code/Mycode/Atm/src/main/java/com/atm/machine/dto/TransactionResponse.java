package com.atm.machine.dto;

import com.atm.machine.entity.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO for responding to a financial transaction request.
 */
@Data
@Builder
public class TransactionResponse {

    private Long transactionId;
    private TransactionType transactionType;
    private String currencyCode; // ADDED: Currency of the transaction
    private BigDecimal amount;
    private String status;
    private BigDecimal newBalance; // The balance after the transaction
    private ZonedDateTime timestamp;
    private String message;
}

