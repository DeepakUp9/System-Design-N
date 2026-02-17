package com.library.enums;

/**
 * Enum representing the status of a book reservation.
 */
public enum ReservationStatus {
    WAITING("Waiting for book to become available"),
    PENDING("Book is available, waiting for member to collect"),
    COMPLETED("Reservation completed, book checked out"),
    CANCELLED("Reservation cancelled"),
    EXPIRED("Reservation expired (not collected within time limit)");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
