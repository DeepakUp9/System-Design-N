package com.example.carrental.service.dto;


import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class ReserveVehicleRequest {
    private final UUID customerId;
    private final UUID vehicleId;
    private final Instant startTime;
    private final Instant endTime;
    private final Set<UUID> equipmentIds;
    private final Set<UUID> serviceIds;
    private final boolean payNow;

    public ReserveVehicleRequest(UUID customerId, UUID vehicleId, Instant startTime,
                                 Instant endTime, Set<UUID> equipmentIds,
                                 Set<UUID> serviceIds, boolean payNow) {
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.equipmentIds = equipmentIds;
        this.serviceIds = serviceIds;
        this.payNow = payNow;
    }

    // Getters
    public UUID customerId() { return customerId; }
    public UUID vehicleId() { return vehicleId; }
    public Instant startTime() { return startTime; }
    public Instant endTime() { return endTime; }
    public Set<UUID> equipmentIds() { return equipmentIds; }
    public Set<UUID> serviceIds() { return serviceIds; }
    public boolean payNow() { return payNow; }
}