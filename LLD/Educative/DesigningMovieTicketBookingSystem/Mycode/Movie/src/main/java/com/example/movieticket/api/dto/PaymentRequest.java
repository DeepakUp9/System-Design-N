package com.example.movieticket.api.dto;

import java.math.BigDecimal;

public class PaymentRequest {
    private Long bookingId;
    private BigDecimal amount;
    private String idempotencyKey;

    // getters & setters
}
