package com.librarymanagement.services;

/**
 * Class diagram: Decorator pattern — concrete component (base fine).
 */
public class BaseFineCalculation implements FineCalculation {
    private static final double FINE_PER_DAY = 1.0;
    private final int overdueDays;

    public BaseFineCalculation(int overdueDays) {
        this.overdueDays = Math.max(0, overdueDays);
    }

    @Override
    public double getAmount() {
        return overdueDays * FINE_PER_DAY;
    }
}
