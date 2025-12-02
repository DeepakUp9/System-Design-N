package com.atm.machine.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    // Many-to-One relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "card_number", unique = true, nullable = false, length = 16)
    private String cardNumber; // This will be the primary input for ATM transactions

    // CRITICAL SECURITY: This stores the HASH of the PIN, not the plaintext PIN.
    @Column(name = "pin_hash", nullable = false, length = 100)
    private String pinHash;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    // CVV is not typically used by ATM, but included for completeness/internal systems
    @Column(name = "cvv_hash", length = 100)
    private String cvvHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status = CardStatus.ACTIVE;

    // Edge Case Field: Used to track attempts to lock out the card
    @Column(name = "pin_fail_count", nullable = false)
    private Integer pinFailCount = 0;

    @Column(name = "max_pin_fail_count", nullable = false)
    private Integer maxPinFailCount = 3; // Default limit

    @Column(name = "date_created", nullable = false)
    private ZonedDateTime dateCreated = ZonedDateTime.now();
}