package com.example.movieticket.domain.model;

import com.example.movieticket.domain.enums.PaymentStatus;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payment_attempt", indexes = {
        @Index(name = "idx_payment_idempotency", columnList = "idempotency_key")
})
public class PaymentAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;

    @Column(name = "gateway_reference")
    private String gatewayReference;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    private BigDecimal amount;

    private Instant createdAt;

    @Version
    private Long version;

    public PaymentAttempt() {}

    public PaymentAttempt(Long bookingId, String idempotencyKey, BigDecimal amount) {
        this.bookingId = bookingId;
        this.idempotencyKey = idempotencyKey;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.createdAt = Instant.now();
    }

    // getters / setters
    public Long getId() { return id; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public String getGatewayReference() { return gatewayReference; }
    public void setGatewayReference(String gatewayReference) { this.gatewayReference = gatewayReference; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Instant getCreatedAt() { return createdAt; }
}
