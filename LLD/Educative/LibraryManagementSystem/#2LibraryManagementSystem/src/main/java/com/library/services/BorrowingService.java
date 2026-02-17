package com.library.services;

import com.library.enums.BookStatus;
import com.library.exceptions.*;
import com.library.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling book borrowing operations.
 * Requirements R7, R8: Max 10 books for 15 days.
 * Requirement R10: Record transactions.
 * 
 * SOLID PRINCIPLE: Single Responsibility Principle
 * - Only responsible for borrowing-related operations
 * - Delegates notifications to NotificationService
 * - Delegates fine calculations to FineService
 */
public class BorrowingService {
    private final List<BookLending> lendingHistory;
    private final NotificationService notificationService;
    
    public BorrowingService(NotificationService notificationService) {
        this.lendingHistory = new ArrayList<>();
        this.notificationService = notificationService;
    }
    
    /**
     * Borrow a book item.
     * Implements complete borrowing workflow with validation.
     * 
     * @param member The member borrowing the book
     * @param bookItem The book item to borrow
     * @param librarian The librarian processing the transaction
     * @return BookLending transaction record
     */
    public BookLending borrowBook(Member member, BookItem bookItem, Librarian librarian) {
        // Validation Step 1: Check member eligibility
        validateMemberEligibility(member);
        
        // Validation Step 2: Check book availability
        validateBookAvailability(bookItem);
        
        // Calculate due date (R8: 15 days)
        LocalDate dueDate = LocalDate.now().plusDays(Member.getBorrowingPeriodDays());
        
        // Update book item state
        bookItem.checkout(member, dueDate);
        
        // Update member state
        member.addBorrowedBook(bookItem);
        
        // Create lending record (R10: Record transaction)
        BookLending lending = new BookLending(member, bookItem, librarian, dueDate);
        lendingHistory.add(lending);
        
        System.out.println("✅ Book borrowed successfully:");
        System.out.println("   Member: " + member.getName());
        System.out.println("   Book: " + bookItem.getBook().getTitle());
        System.out.println("   Due Date: " + dueDate);
        System.out.println("   Book Item ID: " + bookItem.getBookItemId());
        System.out.println();
        
        // Send reminder notification (3 days before due date)
        // In production, this would be scheduled
        notificationService.sendDueDateReminder(member, bookItem, Member.getBorrowingPeriodDays() - 3);
        
        return lending;
    }
    
    /**
     * Return a borrowed book.
     * Handles fine calculation and reservation processing.
     * 
     * @param member The member returning the book
     * @param bookItem The book item being returned
     * @param librarian The librarian processing the return
     * @param fineService Service for fine calculation
     * @return Fine object if overdue, null otherwise
     */
    public Fine returnBook(Member member, BookItem bookItem, Librarian librarian, FineService fineService) {
        // Find the lending record
        BookLending lending = findActiveLending(member, bookItem);
        if (lending == null) {
            throw new LibraryException("No active lending found for this book and member");
        }
        
        // Mark as returned
        lending.markAsReturned(librarian);
        
        // Remove from member's borrowed books
        member.removeBorrowedBook(bookItem);
        
        // Calculate fine if overdue
        Fine fine = null;
        if (lending.isOverdue()) {
            fine = fineService.calculateAndIssueFine(member, lending);
            System.out.println("⚠️  Book returned late. Fine issued: $" + fine.getAmount());
            notificationService.sendOverdueNotification(member, bookItem);
        }
        
        // Update book item state and process reservations
        bookItem.returnBook();
        
        // Check for reservations
        if (bookItem.hasReservations()) {
            BookReservation nextReservation = bookItem.getNextReservation();
            if (nextReservation != null) {
                nextReservation.markAsAvailable();
                notificationService.sendReservationAvailableNotification(
                    nextReservation.getMember(), 
                    bookItem
                );
                System.out.println("📢 Notification sent to next member in reservation queue");
            }
        }
        
        System.out.println("✅ Book returned successfully:");
        System.out.println("   Member: " + member.getName());
        System.out.println("   Book: " + bookItem.getBook().getTitle());
        System.out.println("   Return Date: " + LocalDate.now());
        if (fine != null) {
            System.out.println("   Fine Amount: $" + fine.getAmount());
        }
        System.out.println();
        
        return fine;
    }
    
    /**
     * Validate member eligibility for borrowing.
     * R7: Check borrowing limit (max 10 books).
     * 
     * @param member The member to validate
     */
    private void validateMemberEligibility(Member member) {
        if (!member.isActive()) {
            throw new LibraryException("Member account is not active");
        }
        
        if (!member.canBorrowBooks()) {
            if (member.getBorrowedBooksCount() >= Member.getMaxBooksAllowed()) {
                throw new BorrowLimitExceededException(
                    "Member has reached maximum borrowing limit of " + Member.getMaxBooksAllowed() + " books"
                );
            }
            if (member.hasFines()) {
                throw new OutstandingFinesException(
                    "Member has outstanding fines of $" + member.getOutstandingFines() + 
                    ". Please clear fines before borrowing."
                );
            }
        }
    }
    
    /**
     * Validate book availability for borrowing.
     * 
     * @param bookItem The book item to validate
     */
    private void validateBookAvailability(BookItem bookItem) {
        if (!bookItem.isAvailable()) {
            throw new BookNotAvailableException(
                "Book item is not available. Current status: " + bookItem.getStatus()
            );
        }
    }
    
    /**
     * Find active lending record for a member and book item.
     * 
     * @param member The member
     * @param bookItem The book item
     * @return Active lending record or null
     */
    private BookLending findActiveLending(Member member, BookItem bookItem) {
        return lendingHistory.stream()
                .filter(lending -> lending.getMember().equals(member) &&
                                 lending.getBookItem().equals(bookItem) &&
                                 !lending.isReturned())
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get all lending records.
     * 
     * @return List of all lending records
     */
    public List<BookLending> getAllLendings() {
        return new ArrayList<>(lendingHistory);
    }
    
    /**
     * Get active lendings for a member.
     * 
     * @param member The member
     * @return List of active lendings
     */
    public List<BookLending> getActiveLendings(Member member) {
        return lendingHistory.stream()
                .filter(lending -> lending.getMember().equals(member) && !lending.isReturned())
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get overdue lendings.
     * 
     * @return List of overdue lendings
     */
    public List<BookLending> getOverdueLendings() {
        return lendingHistory.stream()
                .filter(lending -> !lending.isReturned() && lending.isOverdue())
                .collect(java.util.stream.Collectors.toList());
    }
}
