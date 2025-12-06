package com.hms.hotel.service;

import com.hms.hotel.entity.Booking;
import com.hms.hotel.entity.Status;
import com.hms.hotel.event.BookingConfirmedEvent;
import com.hms.hotel.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ApplicationEventPublisher eventPublisher; // LLD: The Publisher/Subject

    // Injecting the Publisher
    public BookingService(BookingRepository bookingRepository, ApplicationEventPublisher eventPublisher) {
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Core business logic to confirm a booking and trigger side effects.
     */
    @Transactional
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        // 1. Core Logic: Change status and persist
        if (booking.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Booking must be PENDING to be confirmed.");
        }
        booking.updateStatus(Status.CONFIRMED);
        booking = bookingRepository.save(booking);

        // 2. LLD: Publish the Event (Decoupling Side Effects)
        BookingConfirmedEvent event = new BookingConfirmedEvent(this, booking);
        eventPublisher.publishEvent(event);

        // This line immediately returns, while email sending and audit processing run asynchronously or transactionally.
        return booking;
    }

    // ... other booking CRUD methods (e.g., create, cancel)
}