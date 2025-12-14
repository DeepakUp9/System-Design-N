package com.stockbrokerage.finance.service;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.user.model.AccountType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service Layer: Calculates all regulatory fees and brokerage commissions.
 */
@Service
public class CommissionCalculator {

    // Production detail: Fee structure would be loaded from a configuration or database table
    private static final BigDecimal BASE_COMMISSION = new BigDecimal("4.95");
    private static final BigDecimal PER_SHARE_FEE = new BigDecimal("0.005");
    private static final BigDecimal REGULATORY_FEE_RATE = new BigDecimal("0.0000207"); // SEC rate example

    /**
     * Calculates the total cost (or proceeds) of a trade, including all fees.
     * @param order The order entity.
     * @return The total fees to be charged (a negative BigDecimal).
     */
    public BigDecimal calculateCommissionAndFees(Order order, AccountType accountType) {

        // 1. Brokerage Commission (Simplified: Flat fee + per share fee)
        BigDecimal commission = BASE_COMMISSION;

        // Optional: Add per-share fee only for active traders
        if (accountType == AccountType.MARGIN) {
            BigDecimal shareFee = order.getQuantity().multiply(PER_SHARE_FEE).setScale(2, RoundingMode.HALF_UP);
            commission = commission.add(shareFee);
        }

        // 2. Regulatory Fees (SEC Fee example)
        // SEC Fee is charged on SELL transactions. Value of the sale * Regulatory Rate.
        BigDecimal regulatoryFee = BigDecimal.ZERO;
        if (order.getType().name().contains("SELL")) { // Use the action to determine fee type
            BigDecimal tradeValue = order.getQuantity().multiply(order.getCurrentPrice());
            regulatoryFee = tradeValue.multiply(REGULATORY_FEE_RATE).setScale(4, RoundingMode.UP); // Fees are rounded up
        }

        BigDecimal totalFees = commission.add(regulatoryFee).setScale(2, RoundingMode.HALF_UP);
        System.out.printf("[FEES] Commission: %s, Regulatory Fee: %s. Total Fees: %s%n", commission, regulatoryFee, totalFees);

        // Fees are a cost, so they are returned as a negative amount to be deducted from the account.
        return totalFees.negate();
    }
}