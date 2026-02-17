package com.librarymanagement.models;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Library card with unique card number (R6). One per User.
 * Design pattern: Factory — unique card number via AtomicLong (thread-safe).
 * Composition: User has-one LibraryCard. Encapsulation: private fields.
 */
public class LibraryCard {
    private static final AtomicLong CARD_SEQ = new AtomicLong(1000);
    private final String cardNumber;
    private final Date issueDate;
    private boolean active;

    public LibraryCard() {
        this.cardNumber = "LC" + CARD_SEQ.getAndIncrement();
        this.issueDate = new Date();
        this.active = true;
    }

    public String getCardNumber() { return cardNumber; }
    public Date getIssueDate() { return issueDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
