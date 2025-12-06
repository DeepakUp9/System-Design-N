package com.hms.hotel.listener;

import com.hms.hotel.event.BookingConfirmedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * LLD: Observer Pattern - Concrete Observer (Notification)
 * Handles sending emails asynchronously after a booking is confirmed.
 */
@Component
public class EmailNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationListener.class);

    /**
     * @Async ensures this process runs in a separate thread, not blocking the main transaction.
     * This is critical for production-level decoupling.
     */
    @Async
    @EventListener
    public void handleBookingConfirmation(BookingConfirmedEvent event) {
        // Simulate email sending delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Logic to integrate with an actual Email Service (SMTP, SendGrid, etc.)
        log.info("📧 OBSERVER 1: Sending confirmation email for Booking ID: {} (Room: {})",
                event.getBooking().getId(), event.getBooking().getRoom().getRoomNumber());
        // Edge Case: Handling email failure retry logic would happen here.
    }
}