package com.airline.flight.service;

import com.airline.flight.domain.Flight;
import com.airline.flight.domain.FlightStatus;
import com.airline.flight.observer.FlightStatusSubject;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
@Service
@Slf4j
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightStatusSubject statusSubject;

    // ============================================
    // MAIN METHOD WITH CIRCUIT BREAKER
    // ============================================
    @CircuitBreaker(
            name = "flightUpdateService",  // MUST match application.properties
            fallbackMethod = "fallbackUpdateStatus"
    )
    @Transactional
    public void updateFlightStatus(String flightId, FlightStatus newStatus) {
        log.info("Updating flight {} status to {}", flightId, newStatus);

        // 1. Update database
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found: " + flightId));

        FlightStatus oldStatus = flight.getStatus();
        flight.setStatus(newStatus);
        flightRepository.save(flight);

        log.info("Database updated for flight {}", flightId);

        // 2. Notify observers (THIS COULD FAIL!)
        statusSubject.notifyObservers(flight);

        log.info("Successfully updated flight {} from {} to {}",
                flightId, oldStatus, newStatus);
    }

    // ============================================
    // FALLBACK METHOD (MUST BE PUBLIC!)
    // ============================================
    public void fallbackUpdateStatus(
            String flightId,
            FlightStatus newStatus,
            Exception e) {

        // Log with circuit breaker context
        log.error("⛔ CIRCUIT BREAKER TRIGGERED for flight {}: {}", flightId, e.getMessage());
        log.error("Exception type: {}", e.getClass().getName());
        log.error("Exception stack trace:", e);  // Full stack trace

        // ============================================
        // SAFE FALLBACK ACTIONS (Choose one or more)
        // ============================================

        // Option 1: Log to database for manual intervention
        logToAuditTable(flightId, newStatus, "CIRCUIT_BREAKER", e.getMessage());

        // Option 2: Save to retry queue (Redis/Database)
        saveToRetryQueue(flightId, newStatus);

        // Option 3: Send alert to operations team
        sendAlertToOps(flightId, newStatus, e);

        // Option 4: Update database ONLY (skip Kafka)
        updateDatabaseOnly(flightId, newStatus);

        log.warn("Flight {} update queued for retry due to: {}", flightId, e.getMessage());
    }

    // ============================================
    // HELPER METHODS FOR FALLBACK
    // ============================================

    private void logToAuditTable(String flightId, FlightStatus status,
                                 String reason, String error) {
        // Implement database audit logging
        // This should NEVER fail (local operation)
    }

    private void saveToRetryQueue(String flightId, FlightStatus status) {
        // Save to Redis or database table for later retry
        // Example: retryQueueService.queue(flightId, status);
    }

    private void sendAlertToOps(String flightId, FlightStatus status, Exception e) {
        // Send email/SMS/Teams/Slack alert
        // alertService.send("Flight update failed: " + flightId);
    }

    private void updateDatabaseOnly(String flightId, FlightStatus newStatus) {
        try {
            // Try to at least update the database
            Flight flight = flightRepository.findById(flightId).orElse(null);
            if (flight != null) {
                flight.setStatus(newStatus);
                flightRepository.save(flight);
                log.info("Updated database only for flight {}", flightId);
            }
        } catch (Exception dbEx) {
            log.error("Even database update failed: {}", dbEx.getMessage());
        }
    }
}