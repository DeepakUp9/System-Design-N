package com.hms.hotel.listener;

import com.hms.hotel.event.BookingConfirmedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * LLD: Observer Pattern - Concrete Observer (Auditing)
 * Handles creating an audit log only after the main transaction commits successfully.
 */
@Component
public class AuditTrailListener {

    private static final Logger log = LoggerFactory.getLogger(AuditTrailListener.class);

    /**
     * @TransactionalEventListener ensures this listener is only executed
     * AFTER the database transaction commits successfully. If the main transaction
     * fails, this event is never processed, maintaining data integrity (Edge Case covered).
     */
    @TransactionalEventListener
    public void handleBookingAudit(BookingConfirmedEvent event) {
        // Logic to write to a dedicated Audit Log table or third-party monitoring system
        log.info("💾 OBSERVER 2: AUDIT LOG created for successful Booking confirmation.");
        log.info("   Details: Booking ID: {}, Status: CONFIRMED, Price: {}",
                event.getBooking().getId(), event.getBooking().getTotalPrice());
    }
}