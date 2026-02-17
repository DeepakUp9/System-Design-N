package com.library.models;

import com.library.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a book reservation.
 * Requirement R9: Only one member can reserve each book item at a time.
 * Requirement R10: Record who reserved and when.
 * Requirement R13: Members can reserve unavailable books.
 * 
 * Implements FIFO (First In, First Out) queue mechanism for fair reservation handling.
 */
public class BookReservation {
    private final String reservationId;
    private final Member member;
    private final BookItem bookItem;
    private final LocalDate reservationDate;
    private ReservationStatus status;
    private LocalDate notificationDate;
    private LocalDate expiryDate;
    
    // Constants
    private static final int HOLD_PERIOD_DAYS = 1; // 24 hours to collect

    public BookReservation(Member member, BookItem bookItem) {
        this.reservationId = generateReservationId();
        this.member = member;
        this.bookItem = bookItem;
        this.reservationDate = LocalDate.now();
        this.status = ReservationStatus.WAITING;
    }

    private String generateReservationId() {
        return "RES-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getters
    public String getReservationId() {
        return reservationId;
    }

    public Member getMember() {
        return member;
    }

    public BookItem getBookItem() {
        return bookItem;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDate getNotificationDate() {
        return notificationDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    // Business logic methods
    public void markAsAvailable() {
        this.status = ReservationStatus.PENDING;
        this.notificationDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusDays(HOLD_PERIOD_DAYS);
    }

    public void complete() {
        this.status = ReservationStatus.COMPLETED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    public void expire() {
        this.status = ReservationStatus.EXPIRED;
    }

    public boolean isExpired() {
        return expiryDate != null && LocalDate.now().isAfter(expiryDate);
    }

    public boolean isActive() {
        return status == ReservationStatus.WAITING || status == ReservationStatus.PENDING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookReservation that = (BookReservation) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

    @Override
    public String toString() {
        return "BookReservation{" +
                "reservationId='" + reservationId + '\'' +
                ", member=" + member.getName() +
                ", bookItem=" + bookItem.getBook().getTitle() +
                ", status=" + status +
                ", reservationDate=" + reservationDate +
                '}';
    }
}
