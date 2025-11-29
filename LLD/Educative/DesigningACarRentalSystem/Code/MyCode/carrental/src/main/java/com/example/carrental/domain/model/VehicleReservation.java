package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
public class VehicleReservation {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "pickup_branch_id")
    private CarRentalBranch pickupBranch;

    @ManyToOne
    @JoinColumn(name = "dropoff_branch_id")
    private CarRentalBranch dropoffBranch;

    private Instant startTime;
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Instant createdAt = Instant.now();

    @Column(name = "estimated_cost")
    private Double estimatedCost;

    @ManyToMany
    @JoinTable(
            name = "reservation_equipment",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private Set<Equipment> equipments;

    @ManyToMany
    @JoinTable(
            name = "reservation_service",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Service> services;
}
