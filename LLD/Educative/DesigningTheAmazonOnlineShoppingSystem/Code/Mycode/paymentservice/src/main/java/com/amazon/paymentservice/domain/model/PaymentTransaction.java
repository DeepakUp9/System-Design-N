package com.amazon.paymentservice.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId; // Reference to the Order Service

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String paymentMethod; // e.g., CREDIT_CARD, PAYPAL (Strategy Name)

    private String gatewayTransactionId; // ID returned by the external gateway

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status; // SUCCESS, FAILED, PENDING

    private LocalDateTime transactionDate;

    // Getters and Setters Omitted
}