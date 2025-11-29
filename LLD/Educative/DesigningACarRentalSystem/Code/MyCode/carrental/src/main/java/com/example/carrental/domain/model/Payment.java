package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private VehicleReservation reservation;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    private BigDecimal amount;
    private String currency;
    private String method;
    private String status;
    private Instant createdAt = Instant.now();
}
