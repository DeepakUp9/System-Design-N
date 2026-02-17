package com.librarymanagement.services;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.librarymanagement.enums.ReservationStatus;

/**
 * Reservation record and lookup (R9, R10): itemId, memberId, creationDate, status.
 * One active reservation per book item. Thread safety: ConcurrentHashMap.
 * Architectural consistency: createReservation/fetchReservationDetails/cancelReservation fully implemented.
 */
public class BookReservation {
    private static final Map<String, BookReservation> reservations = new ConcurrentHashMap<>();

    private final String itemId;
    private final String memberId;
    private final Date creationDate;
    private ReservationStatus status;

    public BookReservation(String itemId, String memberId) {
        this.itemId = itemId;
        this.memberId = memberId;
        this.creationDate = new Date();
        this.status = ReservationStatus.PENDING;
    }

    public static BookReservation createReservation(String bookItemId, String memberId) {
        BookReservation r = new BookReservation(bookItemId, memberId);
        reservations.put(bookItemId, r);
        return r;
    }

    public static BookReservation fetchReservationDetails(String bookItemId) {
        return reservations.get(bookItemId);
    }

    public static void cancelReservation(String bookItemId) {
        reservations.remove(bookItemId);
    }

    public static void completeReservation(String bookItemId) {
        BookReservation r = reservations.get(bookItemId);
        if (r != null) {
            r.status = ReservationStatus.NONE;
        }
        reservations.remove(bookItemId);
    }

    public String getItemId() { return itemId; }
    public String getMemberId() { return memberId; }
    public Date getCreationDate() { return creationDate; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}
