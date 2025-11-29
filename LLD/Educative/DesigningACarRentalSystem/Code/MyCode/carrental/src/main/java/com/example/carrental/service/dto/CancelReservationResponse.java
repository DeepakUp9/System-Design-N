package com.example.carrental.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CancelReservationResponse {
    private final UUID reservationId;
    private final boolean success;
    private final String message;
    private final BigDecimal refundAmount;
    private final String currency;

    public CancelReservationResponse(UUID reservationId, boolean success, String message,
                                     BigDecimal refundAmount, String currency) {
        this.reservationId = reservationId;
        this.success = success;
        this.message = message;
        this.refundAmount = refundAmount;
        this.currency = currency;
    }

    // Getters
    public UUID reservationId() { return reservationId; }
    public boolean success() { return success; }
    public String message() { return message; }
    public BigDecimal refundAmount() { return refundAmount; }
    public String currency() { return currency; }
}