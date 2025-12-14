package com.stockbrokerage.audit;

import com.stockbrokerage.order.event.OrderProcessedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async; // Production Detail: Event processing should be non-blocking
import org.springframework.stereotype.Service;

/**
 * Concrete Observer 1: Handles mandatory, transactional auditing for every state change.
 * This is crucial for regulatory compliance (FINRA, SEC).
 */
@Service
public class AuditServiceListener {

    // Dependency Injection (e.g., AuditRepository, ComplianceService) would be here

    @EventListener
    @Async // Run this in a separate thread so it doesn't block the core order execution.
    public void handleOrderEvent(OrderProcessedEvent event) {

        // Production Logic:
        System.out.printf("[AUDIT LOG - ASYNC] Order %s transitioned from %s to %s at %s. Account: %d%n",
                event.getOrder().getOrderReferenceId(),
                event.getPreviousStatus(),
                event.getCurrentStatus(),
                event.getTimestamp(),
                event.getOrder().getAccountId());

        // Full production implementation would involve:
        // 1. Mapping event data to an AuditLog entity.
        // 2. Persisting the AuditLog entity to a dedicated, secured audit database/table.
        // 3. Checking for specific compliance flags related to the transition.

        // Mock persistence delay to simulate real-world I/O
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}
    }
}