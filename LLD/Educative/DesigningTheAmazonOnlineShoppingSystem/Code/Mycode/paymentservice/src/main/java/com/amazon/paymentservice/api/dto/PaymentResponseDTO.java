package com.amazon.paymentservice.api.dto;

import com.amazon.paymentservice.domain.model.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDTO(
        Long transactionId,
        Long orderId,
        BigDecimal amount,
        String paymentMethod,
        TransactionStatus status, // SUCCESS, FAILED, PENDING
        String gatewayTransactionId,
        LocalDateTime transactionDate
) {}