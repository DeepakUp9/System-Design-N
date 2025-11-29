package com.example.carrental.service.dto;


import java.time.Instant;
import java.util.UUID;

public class ReserveVehicleResponse {
    private final UUID reservationId;
    private final UUID vehicleId;
    private final UUID customerId;
    private final Instant startTime;
    private final Instant endTime;
    private final String status;

    public ReserveVehicleResponse(UUID reservationId, UUID vehicleId, UUID customerId,
                                  Instant startTime, Instant endTime, String status) {
        this.reservationId = reservationId;
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    // Getters
    public UUID reservationId() { return reservationId; }
    public UUID vehicleId() { return vehicleId; }
    public UUID customerId() { return customerId; }
    public Instant startTime() { return startTime; }
    public Instant endTime() { return endTime; }
    public String status() { return status; }
}
