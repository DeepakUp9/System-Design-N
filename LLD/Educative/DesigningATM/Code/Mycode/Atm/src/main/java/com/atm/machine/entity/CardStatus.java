package com.atm.machine.entity;

/**
 * Enumeration for the status of an ATM/Debit card.
 * Matches the 'card_status' ENUM in PostgreSQL.
 */
public enum CardStatus {
    ACTIVE,
    BLOCKED, // Used for manual blocks or PIN lockout
    EXPIRED
}