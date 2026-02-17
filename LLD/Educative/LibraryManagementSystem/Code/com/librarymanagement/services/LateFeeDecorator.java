package com.librarymanagement.services;

/**
 * Class diagram: Decorator pattern — concrete decorator.
 * Adds extra fee for extended overdue (e.g. after 7 days, higher rate).
 */
public class LateFeeDecorator extends FineDecorator {
    private static final double EXTRA_RATE_AFTER_DAYS = 0.5;
    private final int daysOverdue;

    public LateFeeDecorator(FineCalculation wrapped, int daysOverdue) {
        super(wrapped);
        this.daysOverdue = Math.max(0, daysOverdue);
    }

    @Override
    protected double getExtraAmount() {
        if (daysOverdue <= 7) return 0;
        return (daysOverdue - 7) * EXTRA_RATE_AFTER_DAYS;
    }
}
