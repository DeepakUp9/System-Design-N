package com.hms.hotel.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "guest")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String loyaltyId; // Optional, used by LoyaltyMemberRateStrategy

    // One Guest can have many Bookings, one Booking has one Primary Guest
    // We would typically manage this relationship in the Booking entity.

    // Getters and Setters (omitted for brevity)
    public Long getId() { return id; }
    public String getEmail() { return email; }
    // ...
}