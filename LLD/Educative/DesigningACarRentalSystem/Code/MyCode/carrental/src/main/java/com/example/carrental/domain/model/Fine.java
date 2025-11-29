package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "fine")
@NoArgsConstructor
@AllArgsConstructor
public class Fine {
    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private VehicleReservation reservation;

    private BigDecimal amount;
    private String reason;
    private Instant createdAt = Instant.now();
}
