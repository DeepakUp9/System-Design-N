package com.library.enums;

/**
 * Enum representing different types of transactions in the library system.
 */
public enum TransactionType {
    BORROW("Book borrowing transaction"),
    RETURN("Book return transaction"),
    RESERVE("Book reservation transaction"),
    RENEW("Book renewal transaction"),
    FINE_PAYMENT("Fine payment transaction");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
