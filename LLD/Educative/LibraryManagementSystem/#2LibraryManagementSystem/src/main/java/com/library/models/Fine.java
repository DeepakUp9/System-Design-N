package com.library.models;

import com.library.enums.FineStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a fine issued to a member.
 * Tracks fine amount, payment status, and related lending transaction.
 * 
 * DESIGN PATTERN: Value Object for fine calculation
 * - Immutable once created (except status changes)
 */
public class Fine {
    private final String fineId;
    private final Member member;
    private final BookLending bookLending;
    private final double amount;
    private final LocalDate issueDate;
    private FineStatus status;
    private LocalDate paymentDate;
    private String reason;
    
    // Constants
    private static final double DAILY_FINE_RATE = 1.0; // $1 per day
    private static final double MAX_FINE_PER_BOOK = 50.0;

    public Fine(Member member, BookLending bookLending, double amount, String reason) {
        this.fineId = generateFineId();
        this.member = member;
        this.bookLending = bookLending;
        this.amount = Math.min(amount, MAX_FINE_PER_BOOK); // Cap at maximum
        this.issueDate = LocalDate.now();
        this.status = FineStatus.PENDING;
        this.reason = reason;
    }

    private String generateFineId() {
        return "FINE-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Static factory method for calculating overdue fine
    public static Fine createOverdueFine(Member member, BookLending bookLending) {
        long overdueDays = bookLending.getOverdueDays();
        double amount = overdueDays * DAILY_FINE_RATE;
        String reason = "Overdue return - " + overdueDays + " days late";
        return new Fine(member, bookLending, amount, reason);
    }

    // Getters
    public String getFineId() {
        return fineId;
    }

    public Member getMember() {
        return member;
    }

    public BookLending getBookLending() {
        return bookLending;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public FineStatus getStatus() {
        return status;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static double getDailyFineRate() {
        return DAILY_FINE_RATE;
    }

    public static double getMaxFinePerBook() {
        return MAX_FINE_PER_BOOK;
    }

    // Business logic methods
    public void markAsPaid() {
        if (status == FineStatus.PAID) {
            throw new IllegalStateException("Fine is already paid");
        }
        this.status = FineStatus.PAID;
        this.paymentDate = LocalDate.now();
    }

    public void waive() {
        if (status == FineStatus.PAID) {
            throw new IllegalStateException("Cannot waive a paid fine");
        }
        this.status = FineStatus.WAIVED;
    }

    public boolean isPending() {
        return status == FineStatus.PENDING;
    }

    public boolean isPaid() {
        return status == FineStatus.PAID;
    }

    public boolean isWaived() {
        return status == FineStatus.WAIVED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fine fine = (Fine) o;
        return Objects.equals(fineId, fine.fineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fineId);
    }

    @Override
    public String toString() {
        return "Fine{" +
                "fineId='" + fineId + '\'' +
                ", member=" + member.getName() +
                ", amount=$" + String.format("%.2f", amount) +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                ", issueDate=" + issueDate +
                '}';
    }
}
