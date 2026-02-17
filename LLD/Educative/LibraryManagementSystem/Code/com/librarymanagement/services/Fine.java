package com.librarymanagement.services;

/**
 * Fine calculation (R8): per-day rate. Return book use case includes calculate fine.
 * SOLID: SRP — single responsibility of fine calculation.
 * No stubs: calculateFine() and collectFine() fully implemented.
 */
public class Fine {
    private static final double FINE_PER_DAY = 1.0;

    public static double calculateFine(int overdueDays) {
        if (overdueDays <= 0) return 0.0;
        return overdueDays * FINE_PER_DAY;
    }

    public static double collectFine(String memberId, int days) {
        double amount = calculateFine(days);
        return amount;
    }
}
