package com.librarymanagement.models;

import java.util.Date;

import com.librarymanagement.enums.TransactionType;
import com.librarymanagement.users.Librarian;
import com.librarymanagement.users.Member;

/**
 * Immutable transaction record (R1, R10): who, what, when, type, processedBy.
 * State management: no setters; full object created in constructor (architectural consistency).
 * Design: Immutable value object for audit trail.
 */
public class TransactionLog {
    private final String transactionId;
    private final TransactionType type;
    private final Member member;
    private final BookItem bookItem;
    private final Date transactionDate;
    private final Librarian processedBy;

    public TransactionLog(TransactionType type, Member member, BookItem bookItem, Librarian processedBy) {
        this.transactionId = "TXN" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        this.type = type;
        this.member = member;
        this.bookItem = bookItem;
        this.transactionDate = new Date();
        this.processedBy = processedBy;
    }

    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public Member getMember() { return member; }
    public BookItem getBookItem() { return bookItem; }
    public Date getTransactionDate() { return transactionDate; }
    public Librarian getProcessedBy() { return processedBy; }
}
