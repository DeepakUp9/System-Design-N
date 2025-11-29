package com.example.carrental.domain.service;

import com.example.carrental.domain.model.Payment;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface PaymentService {

    /**
     * Capture a payment (charge) or hold depending on method.
     * Must be idempotent when idempotencyKey is provided.
     *
     * @return transactionId
     */
    String capture(UUID reservationId, BigDecimal amount, String currency, String method, String idempotencyKey);

    /**
     * Refund a previously captured payment. Idempotent when idempotencyKey provided.
     * Returns refund transaction id or throws PaymentFailedException
     */
    String refund(UUID paymentId, BigDecimal amount, String idempotencyKey);


}
