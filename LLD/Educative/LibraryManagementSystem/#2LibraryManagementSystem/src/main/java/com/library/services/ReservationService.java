package com.library.services;

import com.library.enums.ReservationStatus;
import com.library.exceptions.BookReservedException;
import com.library.exceptions.LibraryException;
import com.library.models.BookItem;
import com.library.models.BookReservation;
import com.library.models.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for handling book reservation operations.
 * Requirement R9: Only one member can reserve each book item at a time.
 * Requirement R13: Members can reserve unavailable books.
 * 
 * Implements FIFO (First In, First Out) queue mechanism.
 * 
 * SOLID PRINCIPLE: Single Responsibility Principle
 * - Handles only reservation-related operations
 */
public class ReservationService {
    private final List<BookReservation> reservationHistory;
    private final NotificationService notificationService;
    
    public ReservationService(NotificationService notificationService) {
        this.reservationHistory = new ArrayList<>();
        this.notificationService = notificationService;
    }
    
    /**
     * Reserve a book item for a member.
     * Requirement R13: Allow reservation when book is unavailable.
     * Requirement R9: FIFO queue, one active reservation per member per book.
     * 
     * @param member The member making the reservation
     * @param bookItem The book item to reserve
     * @return BookReservation object
     */
    public BookReservation reserveBook(Member member, BookItem bookItem) {
        // Validation: Check if member is active
        if (!member.isActive()) {
            throw new LibraryException("Member account is not active");
        }
        
        // Validation: Check if book can be reserved
        if (!bookItem.canBeReserved()) {
            throw new BookReservedException(
                "Book item cannot be reserved. Current status: " + bookItem.getStatus()
            );
        }
        
        // Validation: Check if member already has active reservation for this book
        if (hasActiveReservation(member, bookItem)) {
            throw new BookReservedException(
                "Member already has an active reservation for this book"
            );
        }
        
        // Create reservation
        BookReservation reservation = new BookReservation(member, bookItem);
        
        // Add to queue (FIFO)
        bookItem.addReservation(reservation);
        reservationHistory.add(reservation);
        
        System.out.println("✅ Book reserved successfully:");
        System.out.println("   Member: " + member.getName());
        System.out.println("   Book: " + bookItem.getBook().getTitle());
        System.out.println("   Position in queue: " + bookItem.getReservationCount());
        System.out.println("   Reservation ID: " + reservation.getReservationId());
        System.out.println();
        
        return reservation;
    }
    
    /**
     * Cancel a reservation.
     * 
     * @param reservation The reservation to cancel
     * @param notifyMember Whether to send notification to member
     */
    public void cancelReservation(BookReservation reservation, boolean notifyMember) {
        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new LibraryException("Cannot cancel a completed reservation");
        }
        
        reservation.cancel();
        
        // Remove from book item's queue
        BookItem bookItem = reservation.getBookItem();
        bookItem.getReservationQueue().remove(reservation);
        
        System.out.println("❌ Reservation cancelled:");
        System.out.println("   Member: " + reservation.getMember().getName());
        System.out.println("   Book: " + bookItem.getBook().getTitle());
        System.out.println();
        
        if (notifyMember) {
            notificationService.sendReservationCancelledNotification(
                reservation.getMember(), 
                bookItem
            );
        }
    }
    
    /**
     * Complete a reservation (when member checks out the book).
     * 
     * @param reservation The reservation to complete
     */
    public void completeReservation(BookReservation reservation) {
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new LibraryException(
                "Reservation must be in PENDING status to complete. Current status: " + reservation.getStatus()
            );
        }
        
        reservation.complete();
        
        System.out.println("✅ Reservation completed:");
        System.out.println("   Member: " + reservation.getMember().getName());
        System.out.println("   Book: " + reservation.getBookItem().getBook().getTitle());
        System.out.println();
    }
    
    /**
     * Process expired reservations.
     * Should be called periodically (e.g., daily job).
     */
    public void processExpiredReservations() {
        List<BookReservation> expiredReservations = reservationHistory.stream()
                .filter(r -> r.getStatus() == ReservationStatus.PENDING && r.isExpired())
                .collect(Collectors.toList());
        
        for (BookReservation reservation : expiredReservations) {
            reservation.expire();
            
            BookItem bookItem = reservation.getBookItem();
            bookItem.getReservationQueue().remove(reservation);
            
            System.out.println("⏰ Reservation expired:");
            System.out.println("   Member: " + reservation.getMember().getName());
            System.out.println("   Book: " + bookItem.getBook().getTitle());
            
            // Process next reservation if available
            if (bookItem.hasReservations()) {
                BookReservation nextReservation = bookItem.peekNextReservation();
                if (nextReservation != null) {
                    nextReservation.markAsAvailable();
                    notificationService.sendReservationAvailableNotification(
                        nextReservation.getMember(), 
                        bookItem
                    );
                    System.out.println("   Next member notified");
                }
            }
            System.out.println();
        }
        
        if (!expiredReservations.isEmpty()) {
            System.out.println("Processed " + expiredReservations.size() + " expired reservations");
        }
    }
    
    /**
     * Check if member has active reservation for a book item.
     * 
     * @param member The member
     * @param bookItem The book item
     * @return true if active reservation exists
     */
    private boolean hasActiveReservation(Member member, BookItem bookItem) {
        return reservationHistory.stream()
                .anyMatch(r -> r.getMember().equals(member) &&
                             r.getBookItem().equals(bookItem) &&
                             r.isActive());
    }
    
    /**
     * Get all reservations for a member.
     * 
     * @param member The member
     * @return List of reservations
     */
    public List<BookReservation> getMemberReservations(Member member) {
        return reservationHistory.stream()
                .filter(r -> r.getMember().equals(member))
                .collect(Collectors.toList());
    }
    
    /**
     * Get active reservations for a member.
     * 
     * @param member The member
     * @return List of active reservations
     */
    public List<BookReservation> getActiveMemberReservations(Member member) {
        return reservationHistory.stream()
                .filter(r -> r.getMember().equals(member) && r.isActive())
                .collect(Collectors.toList());
    }
    
    /**
     * Get all reservation history.
     * 
     * @return List of all reservations
     */
    public List<BookReservation> getAllReservations() {
        return new ArrayList<>(reservationHistory);
    }
}
