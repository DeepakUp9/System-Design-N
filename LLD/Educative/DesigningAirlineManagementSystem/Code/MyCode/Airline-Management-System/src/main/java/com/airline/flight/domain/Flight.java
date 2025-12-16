package com.airline.flight.domain;

import com.airline.core.domain.BaseEntity;
import com.airline.reservation.domain.ReservationStatus; // Reuse if status names match or define new
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "flights", indexes = {
        @Index(name = "idx_flight_number", columnList = "flightNumber"),
        @Index(name = "idx_departure_time", columnList = "departureTime")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String flightNumber;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status;

    private String gate;

    // Production check: Capacity management
    private Integer totalSeats;
    private Integer availableSeats;
}