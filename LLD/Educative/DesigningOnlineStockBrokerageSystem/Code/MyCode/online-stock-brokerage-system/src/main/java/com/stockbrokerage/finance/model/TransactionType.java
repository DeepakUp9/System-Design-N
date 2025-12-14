package com.stockbrokerage.finance.model;

public enum TransactionType {
    // Money Movement
    DEPOSIT,
    WITHDRAWAL,

    // Trading Activity
    BUY,
    SELL,

    // Fees/Adjustments
    COMMISSION,
    REGULATORY_FEE,
    DIVIDEND,
    INTEREST
}