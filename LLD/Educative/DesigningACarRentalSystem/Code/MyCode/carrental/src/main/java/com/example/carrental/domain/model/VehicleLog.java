package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "vehicle_log")
@NoArgsConstructor
@AllArgsConstructor
public class VehicleLog {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private String logType;
    private String description;
    private Instant createdAt = Instant.now();
}
