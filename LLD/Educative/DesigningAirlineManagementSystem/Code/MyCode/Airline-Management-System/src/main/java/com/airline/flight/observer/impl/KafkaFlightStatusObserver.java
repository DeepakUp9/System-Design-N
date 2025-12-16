package com.airline.flight.observer.impl;

import com.airline.flight.domain.Flight;
import com.airline.flight.observer.FlightStatusObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaFlightStatusObserver implements FlightStatusObserver {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "flight-status-changes";

    @Override
    public void onStatusChange(Flight flight) {
        log.info("Publishing flight status change to Kafka for Flight: {}", flight.getFlightNumber());

        // Production Standard: Send a DTO, not the Entity
        FlightStatusEvent event = new FlightStatusEvent(
                flight.getFlightNumber(),
                flight.getStatus(),
                flight.getUpdatedAt()
        );

        kafkaTemplate.send(TOPIC, flight.getFlightNumber(), event);
    }
}