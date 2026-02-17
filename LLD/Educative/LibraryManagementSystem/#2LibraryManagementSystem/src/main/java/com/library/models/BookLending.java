package com.library.models;

import com.library.enums.TransactionType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a book lending/borrowing transaction.
 * Requirement R1: Maintain complete log of borrowing and return transactions.
 * Requirement R10: Record who issued and on which date.
 * 
 * Immutable transaction log for audit trail.
 */
public class BookLending {
    private final String lendingId;
    private final Member member;
    private final BookItem bookItem;
    private final Librarian issuedBy;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private Librarian returnedTo;
    private int renewalCount;
    
    // Constants
    private static final int MAX_RENEWALS = 2;

    public BookLending(Member member, BookItem bookItem, Librarian issuedBy, LocalDate dueDate) {
        this.lendingId = generateLendingId();
        this.member = member;
        this.bookItem = bookItem;
        this.issuedBy = issuedBy;
        this.issueDate = LocalDate.now();
        this.dueDate = dueDate;
        this.renewalCount = 0;
    }

    private String generateLendingId() {
        return "LEND-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getters
    public String getLendingId() {
        return lendingId;
    }

    public Member getMember() {
        return member;
    }

    public BookItem getBookItem() {
        return bookItem;
    }

    public Librarian getIssuedBy() {
        return issuedBy;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Librarian getReturnedTo() {
        return returnedTo;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public void incrementRenewalCount() {
        if (renewalCount >= MAX_RENEWALS) {
            throw new IllegalStateException("Maximum renewals reached");
        }
        this.renewalCount++;
    }

    // Business logic methods
    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue() {
        if (isReturned()) {
            return returnDate.isAfter(dueDate);
        }
        return LocalDate.now().isAfter(dueDate);
    }

    public long getOverdueDays() {
        if (!isOverdue()) {
            return 0;
        }
        
        LocalDate compareDate = isReturned() ? returnDate : LocalDate.now();
        return ChronoUnit.DAYS.between(dueDate, compareDate);
    }

    public void markAsReturned(Librarian returnedTo) {
        if (isReturned()) {
            throw new IllegalStateException("Book is already returned");
        }
        this.returnDate = LocalDate.now();
        this.returnedTo = returnedTo;
    }

    public TransactionType getTransactionType() {
        return TransactionType.BORROW;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookLending that = (BookLending) o;
        return Objects.equals(lendingId, that.lendingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lendingId);
    }

    @Override
    public String toString() {
        return "BookLending{" +
                "lendingId='" + lendingId + '\'' +
                ", member=" + member.getName() +
                ", bookItem=" + bookItem.getBook().getTitle() +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", overdue=" + isOverdue() +
                '}';
    }
}
