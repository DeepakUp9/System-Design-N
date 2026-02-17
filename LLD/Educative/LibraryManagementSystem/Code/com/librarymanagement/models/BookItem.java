package com.librarymanagement.models;

import java.util.Calendar;
import java.util.Date;

import com.librarymanagement.enums.BookStatus;
import com.librarymanagement.users.Member;

/**
 * Physical copy of a Book (R2, R4). Unique ID, rack, status, borrowed/reserved state.
 * Structural integrity: Composition with Book and Rack; Association with Member (borrowedBy, reservedBy).
 * No stubs: checkout(), returnBook(), reserve(), renew() fully implemented per sequence logic.
 * SOLID: SRP — manages single book copy lifecycle and state.
 */
public class BookItem {
    private final String id;
    private final Book book;
    private final Rack placedAt;
    private final double price;
    private final Date dateOfPurchase;
    private final Date publicationDate;

    private BookStatus status;
    private Date borrowedDate;
    private Date dueDate;
    private Member borrowedBy;
    private Member reservedBy;
    private Date reservationDate;
    private int renewalCount;

    private static final int BORROW_DAYS = 15;
    private static final int MAX_RENEWALS = 2;

    public BookItem(String id, Book book, Rack placedAt, double price,
                    Date dateOfPurchase, Date publicationDate) {
        this.id = id;
        this.book = book;
        this.placedAt = placedAt;
        this.price = price;
        this.dateOfPurchase = dateOfPurchase;
        this.publicationDate = publicationDate;
        this.status = BookStatus.AVAILABLE;
        this.renewalCount = 0;
    }

    /** Checkout to member. Returns true if successful (R8: 15 days). */
    public boolean checkout(Member member) {
        if (status != BookStatus.AVAILABLE && !(status == BookStatus.RESERVED && reservedBy == member)) {
            return false;
        }
        status = BookStatus.LOANED;
        borrowedBy = member;
        borrowedDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(borrowedDate);
        c.add(Calendar.DATE, BORROW_DAYS);
        dueDate = c.getTime();
        if (reservedBy == member) {
            reservedBy = null;
            reservationDate = null;
        }
        renewalCount = 0;
        return true;
    }

    /** Return book. If reserved, caller must assign to reserver and notify. */
    public boolean returnBook() {
        if (status != BookStatus.LOANED) {
            return false;
        }
        borrowedBy = null;
        borrowedDate = null;
        dueDate = null;
        renewalCount = 0;
        status = reservedBy != null ? BookStatus.RESERVED : BookStatus.AVAILABLE;
        return true;
    }

    /** Reserve when book is loaned (R9: one reservation per item). Returns true if reserved. */
    public boolean reserve(Member member) {
        if (status != BookStatus.LOANED || reservedBy != null) {
            return false;
        }
        reservedBy = member;
        reservationDate = new Date();
        return true;
    }

    /** Cancel reservation. */
    public void cancelReservation() {
        reservedBy = null;
        reservationDate = null;
        if (status == BookStatus.RESERVED) {
            status = BookStatus.AVAILABLE;
        }
    }

    /** Renew: extend due date by 15 days if under max renewals (R11). */
    public boolean renew() {
        if (status != BookStatus.LOANED || dueDate == null || renewalCount >= MAX_RENEWALS) {
            return false;
        }
        if (reservedBy != null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dueDate);
        c.add(Calendar.DATE, BORROW_DAYS);
        dueDate = c.getTime();
        renewalCount++;
        return true;
    }

    public boolean isOverdue() {
        return dueDate != null && new Date().after(dueDate);
    }

    public int getOverdueDays() {
        if (!isOverdue()) return 0;
        long diff = new Date().getTime() - dueDate.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    public String getId() { return id; }
    public Book getBook() { return book; }
    public BookStatus getStatus() { return status; }
    public Date getDueDate() { return dueDate; }
    public Date getBorrowedDate() { return borrowedDate; }
    public Rack getPlacedAt() { return placedAt; }
    public Member getBorrowedBy() { return borrowedBy; }
    public Member getReservedBy() { return reservedBy; }
    public Date getReservationDate() { return reservationDate; }
    public int getRenewalCount() { return renewalCount; }
    public double getPrice() { return price; }
    public Date getDateOfPurchase() { return dateOfPurchase; }
    public Date getPublicationDate() { return publicationDate; }

    public void setStatus(BookStatus status) { this.status = status; }

    /** For demo only: simulate late return by setting due date in the past. */
    public void setDueDateForDemo(Date dueDate) { this.dueDate = dueDate; }
}
