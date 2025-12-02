package com.atm.machine.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    // Account link: The account affected by this transaction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    // Card link: The card used to initiate the transaction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus = TransactionStatus.PENDING;

    // CRITICAL: Use BigDecimal for amount
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount = BigDecimal.ZERO;

    // Reference (e.g., external account for transfers)
    @Column(name = "reference_account", length = 20)
    private String referenceAccount;

    // Edge Case Field: ATM identification for auditing
    @Column(name = "atm_identifier", length = 50)
    private String atmIdentifier;

    @Column(name = "transaction_timestamp", nullable = false)
    private ZonedDateTime transactionTimestamp = ZonedDateTime.now();
}