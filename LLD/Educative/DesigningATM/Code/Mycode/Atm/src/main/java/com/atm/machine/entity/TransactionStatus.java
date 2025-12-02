package com.atm.machine.entity;

/**
 * Enumeration for the processing status of a transaction.
 * Matches the 'transaction_status' ENUM in PostgreSQL.
 */
public enum TransactionStatus {
    COMPLETED,
    PENDING, // For transactions that require external confirmation (e.g., future transfers)
    FAILED
}