package com.stockbrokerage.order.enums;

public enum OrderStatus {

    // Initial states
    NEW,
    PENDING_VALIDATION,

    // Execution states
    PENDING_EXECUTION,

    // Terminal states
    EXECUTED(true),
    PARTIALLY_EXECUTED(true),
    CANCELLED(true),
    REJECTED(true);

    private final boolean terminal;

    OrderStatus() {
        this(false);
    }

    OrderStatus(boolean terminal) {
        this.terminal = terminal;
    }

    /**
     * Production Utility: Checks if the status is final and cannot be transitioned from.
     * @return true if the status is terminal.
     */
    public boolean isTerminal() {
        return this.terminal;
    }
}