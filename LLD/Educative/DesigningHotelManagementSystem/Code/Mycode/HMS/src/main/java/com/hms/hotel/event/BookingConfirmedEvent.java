package com.hms.hotel.event;

import com.hms.hotel.entity.Booking;
import org.springframework.context.ApplicationEvent;

/**
 * LLD: Observer Pattern - The Concrete Subject/Event
 * Holds the payload (the Booking object) and is published upon confirmation.
 */
public class BookingConfirmedEvent extends ApplicationEvent {

    private final Booking booking;

    public BookingConfirmedEvent(Object source, Booking booking) {
        super(source);
        this.booking = booking;
    }

    public Booking getBooking() {
        return booking;
    }
}