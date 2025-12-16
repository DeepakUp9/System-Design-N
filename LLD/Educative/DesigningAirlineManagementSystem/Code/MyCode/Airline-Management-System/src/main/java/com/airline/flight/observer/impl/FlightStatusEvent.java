package com.airline.flight.observer.impl;

import com.airline.flight.domain.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * This is the DTO sent to Kafka.
 * It is decoupled from the Database Entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightStatusEvent {
    private String flightNumber;
    private FlightStatus status;
    private String gate;
    private LocalDateTime updatedAt;

    // Field for traceability/logging
    private String eventSource = "FLIGHT_SERVICE";
}