package com.example.carrental.service.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class ModifyReservationRequest {
    private final UUID reservationId;
    private final UUID customerId;
    private final Instant newStartTime;
    private final Instant newEndTime;
    private final Set<UUID> newEquipmentIds;
    private final Set<UUID> newServiceIds;

    public ModifyReservationRequest(UUID reservationId, UUID customerId, Instant newStartTime,
                                    Instant newEndTime, Set<UUID> newEquipmentIds,
                                    Set<UUID> newServiceIds) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.newStartTime = newStartTime;
        this.newEndTime = newEndTime;
        this.newEquipmentIds = newEquipmentIds;
        this.newServiceIds = newServiceIds;
    }

    // Getters
    public UUID reservationId() { return reservationId; }
    public UUID customerId() { return customerId; }
    public Instant newStartTime() { return newStartTime; }
    public Instant newEndTime() { return newEndTime; }
    public Set<UUID> newEquipmentIds() { return newEquipmentIds; }
    public Set<UUID> newServiceIds() { return newServiceIds; }
}