package com.atm.machine.entity;

/**
 * Enumeration for the type of transaction initiated at the ATM.
 * Matches the 'transaction_type' ENUM in PostgreSQL.
 */
public enum TransactionType {
    WITHDRAWAL,
    DEPOSIT,
    TRANSFER,
    BALANCE_INQUIRY, // No amount involved, but still a transaction
    FEE
}