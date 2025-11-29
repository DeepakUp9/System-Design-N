package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "vehicle")
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String subtype; // e.g., economy, luxury, etc.

    @Column(unique = true)
    private String licensePlate;

    @Column(unique = true)
    private String vin;

    private String make;
    private String model;
    private Integer year;
    private Long mileage;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_stall_id")
    private ParkingStall parkingStall;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private CarRentalBranch branch;

    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;

    @Column(name = "daily_rate")
    private BigDecimal dailyRate;
}
