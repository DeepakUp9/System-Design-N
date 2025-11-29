package com.example.carrental.service.dto;

import java.util.UUID;

public class CancelReservationRequest {
    private final UUID reservationId;
    private final UUID customerId;
    private final String reason;

    public CancelReservationRequest(UUID reservationId, UUID customerId, String reason) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.reason = reason;
    }

    // Getters (using record-style method names)
    public UUID reservationId() { return reservationId; }
    public UUID customerId() { return customerId; }
    public String reason() { return reason; }
}