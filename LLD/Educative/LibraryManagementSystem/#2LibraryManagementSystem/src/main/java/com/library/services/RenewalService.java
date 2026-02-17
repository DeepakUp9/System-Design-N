package com.library.services;

import com.library.exceptions.*;
import com.library.models.BookItem;
import com.library.models.BookLending;
import com.library.models.Member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling book renewal operations.
 * Requirement R11: Allow renewals within policy limits.
 * 
 * SOLID PRINCIPLE: Single Responsibility Principle
 * - Handles only renewal-related operations
 */
public class RenewalService {
    private static final int MAX_RENEWALS = 2;
    private static final int RENEWAL_PERIOD_DAYS = 15;
    
    private final List<RenewalRecord> renewalHistory;
    
    public RenewalService() {
        this.renewalHistory = new ArrayList<>();
    }
    
    /**
     * Renew a borrowed book.
     * Requirement R11: Renew books according to policy.
     * 
     * @param member The member requesting renewal
     * @param bookItem The book item to renew
     * @param lending The active lending record
     * @return Updated due date
     */
    public LocalDate renewBook(Member member, BookItem bookItem, BookLending lending) {
        // Validation Step 1: Check member ownership
        validateMemberOwnership(member, bookItem, lending);
        
        // Validation Step 2: Check renewal eligibility
        validateRenewalEligibility(member, bookItem, lending);
        
        // Perform renewal
        LocalDate oldDueDate = bookItem.getDueDate();
        bookItem.renew(RENEWAL_PERIOD_DAYS);
        lending.incrementRenewalCount();
        
        LocalDate newDueDate = bookItem.getDueDate();
        
        // Record renewal
        RenewalRecord record = new RenewalRecord(
            member, 
            bookItem, 
            lending, 
            oldDueDate, 
            newDueDate
        );
        renewalHistory.add(record);
        
        System.out.println("✅ Book renewed successfully:");
        System.out.println("   Member: " + member.getName());
        System.out.println("   Book: " + bookItem.getBook().getTitle());
        System.out.println("   Old Due Date: " + oldDueDate);
        System.out.println("   New Due Date: " + newDueDate);
        System.out.println("   Renewal Count: " + lending.getRenewalCount() + " / " + MAX_RENEWALS);
        System.out.println();
        
        return newDueDate;
    }
    
    /**
     * Validate that member is the current borrower.
     * 
     * @param member The member
     * @param bookItem The book item
     * @param lending The lending record
     */
    private void validateMemberOwnership(Member member, BookItem bookItem, BookLending lending) {
        if (!bookItem.getBorrowedBy().equals(member)) {
            throw new LibraryException("Book is not borrowed by this member");
        }
        
        if (!lending.getMember().equals(member)) {
            throw new LibraryException("Lending record does not match member");
        }
        
        if (lending.isReturned()) {
            throw new LibraryException("Cannot renew a returned book");
        }
    }
    
    /**
     * Validate renewal eligibility.
     * Checks: renewal limit, reservations, fines, overdue status.
     * 
     * @param member The member
     * @param bookItem The book item
     * @param lending The lending record
     */
    private void validateRenewalEligibility(Member member, BookItem bookItem, BookLending lending) {
        // Check renewal limit
        if (lending.getRenewalCount() >= MAX_RENEWALS) {
            throw new RenewalLimitExceededException(
                "Maximum renewals (" + MAX_RENEWALS + ") reached for this book"
            );
        }
        
        // Check if book has reservations
        if (bookItem.hasReservations()) {
            throw new BookReservedException(
                "Cannot renew - book is reserved by another member"
            );
        }
        
        // Check member fines
        if (member.hasFines()) {
            throw new OutstandingFinesException(
                "Cannot renew - member has outstanding fines of $" + member.getOutstandingFines()
            );
        }
        
        // Check if book is overdue
        if (bookItem.isOverdue()) {
            throw new LibraryException(
                "Cannot renew an overdue book. Please return it and pay the fine."
            );
        }
        
        // Check member account status
        if (!member.isActive()) {
            throw new LibraryException("Member account is not active");
        }
    }
    
    /**
     * Get renewal history for a member.
     * 
     * @param member The member
     * @return List of renewal records
     */
    public List<RenewalRecord> getMemberRenewalHistory(Member member) {
        return renewalHistory.stream()
                .filter(r -> r.member.equals(member))
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get all renewal records.
     * 
     * @return List of all renewal records
     */
    public List<RenewalRecord> getAllRenewals() {
        return new ArrayList<>(renewalHistory);
    }
    
    /**
     * Inner class to record renewal transactions.
     * Requirement R10: Record all transactions.
     */
    public static class RenewalRecord {
        private final Member member;
        private final BookItem bookItem;
        private final BookLending lending;
        private final LocalDate oldDueDate;
        private final LocalDate newDueDate;
        private final LocalDate renewalDate;
        
        public RenewalRecord(Member member, BookItem bookItem, BookLending lending,
                           LocalDate oldDueDate, LocalDate newDueDate) {
            this.member = member;
            this.bookItem = bookItem;
            this.lending = lending;
            this.oldDueDate = oldDueDate;
            this.newDueDate = newDueDate;
            this.renewalDate = LocalDate.now();
        }
        
        public Member getMember() { return member; }
        public BookItem getBookItem() { return bookItem; }
        public BookLending getLending() { return lending; }
        public LocalDate getOldDueDate() { return oldDueDate; }
        public LocalDate getNewDueDate() { return newDueDate; }
        public LocalDate getRenewalDate() { return renewalDate; }
    }
}
