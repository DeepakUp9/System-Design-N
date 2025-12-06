package com.hms.hotel.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentMethod; // e.g., CREDIT_CARD, PAYPAL, CRYPTO

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // --- Constructor, Getters, Setters (omitted for brevity) ---
    // Standard getters/setters and no-arg constructor...

    public Payment() {}

    public Payment(BigDecimal amount, String paymentMethod, Booking booking) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
        this.booking = booking;
    }

    public BigDecimal getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public Booking getBooking() { return booking; }
}