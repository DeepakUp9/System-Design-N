package com.example.carrental.service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class ModifyReservationResponse {
    private final UUID reservationId;
    private final boolean success;
    private final String message;
    private final Instant newStartTime;
    private final Instant newEndTime;
    private final Set<UUID> equipmentIds;
    private final Set<UUID> serviceIds;
    private final BigDecimal priceDifference;
    private final String currency;

    public ModifyReservationResponse(UUID reservationId, boolean success, String message,
                                     Instant newStartTime, Instant newEndTime,
                                     Set<UUID> equipmentIds, Set<UUID> serviceIds,
                                     BigDecimal priceDifference, String currency) {
        this.reservationId = reservationId;
        this.success = success;
        this.message = message;
        this.newStartTime = newStartTime;
        this.newEndTime = newEndTime;
        this.equipmentIds = equipmentIds;
        this.serviceIds = serviceIds;
        this.priceDifference = priceDifference;
        this.currency = currency;
    }

    // Getters
    public UUID reservationId() { return reservationId; }
    public boolean success() { return success; }
    public String message() { return message; }
    public Instant newStartTime() { return newStartTime; }
    public Instant newEndTime() { return newEndTime; }
    public Set<UUID> equipmentIds() { return equipmentIds; }
    public Set<UUID> serviceIds() { return serviceIds; }
    public BigDecimal priceDifference() { return priceDifference; }
    public String currency() { return currency; }
}