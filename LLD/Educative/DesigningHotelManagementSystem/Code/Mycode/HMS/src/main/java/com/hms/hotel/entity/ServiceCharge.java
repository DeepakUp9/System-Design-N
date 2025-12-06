package com.hms.hotel.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_charge")
public class ServiceCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private String description; // e.g., "Minibar - Water", "Laundry Service"

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime chargeTime;

    // Getters and Setters (omitted for brevity)
    public ServiceCharge() { this.chargeTime = LocalDateTime.now(); }
    public ServiceCharge(Booking booking, String description, BigDecimal amount) {
        this.booking = booking;
        this.description = description;
        this.amount = amount;
        this.chargeTime = LocalDateTime.now();
    }
    // ...
}