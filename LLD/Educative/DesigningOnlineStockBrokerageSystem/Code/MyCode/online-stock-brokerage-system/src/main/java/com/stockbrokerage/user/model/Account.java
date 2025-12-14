package com.stockbrokerage.user.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Production Detail: Account number is often unique and non-sequential
    @Column(unique = true, nullable = false)
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    // Financial Metrics
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO; // Total funds in account

    @Column(nullable = false)
    private BigDecimal availableBalance = BigDecimal.ZERO; // Funds available for trading (after margin/holds)

    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void generateAccountNumber() {
        if (this.accountNumber == null) {
            // Placeholder for production-grade unique ID generation
            this.accountNumber = "ACCT-" + System.currentTimeMillis();
        }
    }
}