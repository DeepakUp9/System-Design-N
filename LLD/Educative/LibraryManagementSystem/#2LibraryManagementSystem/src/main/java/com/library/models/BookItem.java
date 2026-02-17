package com.library.models;

import com.library.enums.BookStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;

/**
 * Entity representing a physical copy of a book in the library.
 * Requirement R2: Each book item has unique ID and physical location.
 * Requirement R4: Multiple book items can exist for one book.
 * 
 * DESIGN PATTERN: State Pattern
 * - BookStatus represents different states of a book item
 * - State transitions are managed through status changes
 */
public class BookItem {
    private final String bookItemId;
    private final Book book; // Reference to parent Book (Composition)
    private BookStatus status;
    private Rack rack;
    private LocalDate dateOfPurchase;
    private double price;
    
    // Borrowing information
    private Member borrowedBy;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private int renewalCount;
    
    // Reservation queue (FIFO)
    private final Queue<BookReservation> reservationQueue;
    
    // Constants
    private static final int MAX_RENEWALS = 2;

    public BookItem(Book book, Rack rack, double price) {
        this.bookItemId = generateBookItemId();
        this.book = book;
        this.rack = rack;
        this.price = price;
        this.status = BookStatus.AVAILABLE;
        this.dateOfPurchase = LocalDate.now();
        this.renewalCount = 0;
        this.reservationQueue = new LinkedList<>();
        
        // Add to parent book and rack
        book.addBookItem(this);
        rack.addBookItem(this);
    }

    private String generateBookItemId() {
        return "BI-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getters
    public String getBookItemId() {
        return bookItemId;
    }

    public Book getBook() {
        return book;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        if (this.rack != null) {
            this.rack.removeBookItem(this);
        }
        this.rack = rack;
        rack.addBookItem(this);
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Member getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(Member borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public void incrementRenewalCount() {
        this.renewalCount++;
    }

    public void resetRenewalCount() {
        this.renewalCount = 0;
    }

    public Queue<BookReservation> getReservationQueue() {
        return reservationQueue;
    }

    // Status check methods (State Pattern implementation)
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    public boolean isBorrowed() {
        return status == BookStatus.BORROWED;
    }

    public boolean isReserved() {
        return status == BookStatus.RESERVED;
    }

    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    public long getOverdueDays() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    public boolean canBeRenewed() {
        return renewalCount < MAX_RENEWALS && 
               !hasReservations() && 
               !isOverdue();
    }

    public boolean canBeReserved() {
        return status == BookStatus.BORROWED;
    }

    // Reservation management
    public void addReservation(BookReservation reservation) {
        reservationQueue.offer(reservation);
    }

    public BookReservation getNextReservation() {
        return reservationQueue.poll();
    }

    public BookReservation peekNextReservation() {
        return reservationQueue.peek();
    }

    public boolean hasReservations() {
        return !reservationQueue.isEmpty();
    }

    public int getReservationCount() {
        return reservationQueue.size();
    }

    /**
     * Checkout this book item to a member.
     * STATE TRANSITION: AVAILABLE -> BORROWED
     */
    public void checkout(Member member, LocalDate dueDate) {
        if (!isAvailable()) {
            throw new IllegalStateException("Book item is not available for checkout");
        }
        
        this.status = BookStatus.BORROWED;
        this.borrowedBy = member;
        this.borrowedDate = LocalDate.now();
        this.dueDate = dueDate;
        this.renewalCount = 0;
    }

    /**
     * Return this book item.
     * STATE TRANSITION: BORROWED -> AVAILABLE or RESERVED
     */
    public void returnBook() {
        if (!isBorrowed()) {
            throw new IllegalStateException("Book item is not borrowed");
        }
        
        this.borrowedBy = null;
        this.borrowedDate = null;
        this.dueDate = null;
        this.renewalCount = 0;
        
        // Check if there are reservations
        if (hasReservations()) {
            this.status = BookStatus.RESERVED;
        } else {
            this.status = BookStatus.AVAILABLE;
        }
    }

    /**
     * Renew this book item.
     * Updates due date while maintaining BORROWED state.
     */
    public void renew(int extensionDays) {
        if (!canBeRenewed()) {
            throw new IllegalStateException("Book item cannot be renewed");
        }
        
        this.dueDate = this.dueDate.plusDays(extensionDays);
        this.renewalCount++;
    }

    public String getFullLocation() {
        return rack != null ? rack.getFullLocation() : "Location not assigned";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookItem bookItem = (BookItem) o;
        return Objects.equals(bookItemId, bookItem.bookItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookItemId);
    }

    @Override
    public String toString() {
        return "BookItem{" +
                "bookItemId='" + bookItemId + '\'' +
                ", book=" + book.getTitle() +
                ", status=" + status +
                ", location=" + getFullLocation() +
                ", borrowedBy=" + (borrowedBy != null ? borrowedBy.getName() : "None") +
                ", dueDate=" + dueDate +
                '}';
    }
}
