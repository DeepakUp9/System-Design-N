package com.atm.machine.service;

import com.atm.machine.dto.TransactionRequest;
import com.atm.machine.entity.Account;
import com.atm.machine.entity.AccountType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service to calculate a real-time Fraud Score for transactions.
 * Uses a simplified rule set based on velocity and amount.
 *
 */
@Service
@Slf4j
public class FraudService {

    // Threshold for high-risk transaction (triggers OTP or block)
    private static final int HIGH_RISK_SCORE_THRESHOLD = 70;

    // In-memory counter for recent activity (Key: Account ID, Value: recent transaction count)
    private final ConcurrentHashMap<Long, AtomicInteger> recentActivity = new ConcurrentHashMap<>();

    /**
     * Calculates a fraud risk score based on transaction details and recent activity.
     * @param account The account performing the transaction.
     * @param request The transaction details (amount, type).
     * @return The calculated risk score (0-100).
     */
    public int calculateRiskScore(Account account, TransactionRequest request) {
        int score = 0;

        // Rule 1: High Transaction Amount
        // Score 30 points if amount > 2000 USD equivalent (for Transfers)
        BigDecimal highAmountThreshold = new BigDecimal("2000.00");
        if (request.getAmount().compareTo(highAmountThreshold) > 0) {
            score += 30;
            log.debug("Rule 1: High amount (> {}) triggered. +30 points.", highAmountThreshold);
        }

        // Rule 2: High Velocity (Multiple transactions in a short period)
        int activityCount = recentActivity.computeIfAbsent(account.getAccountId(), k -> new AtomicInteger(0)).incrementAndGet();
        if (activityCount > 5) {
            score += 20;
            log.debug("Rule 2: High velocity ({} transactions) triggered. +20 points.", activityCount);
        }

        // Rule 3: Account Status Risk (e.g., newly opened or recently had a failed login streak)
        if (account.getAccountType() == AccountType.SAVINGS) {
            // Savings accounts are often less active, so large withdrawals are higher risk
            if (request.getAmount().compareTo(new BigDecimal("500.00")) > 0) {
                score += 15;
                log.debug("Rule 3: Large withdrawal from savings triggered. +15 points.");
            }
        }

        // Note: In a real system, you'd clean up the `recentActivity` map based on time.

        log.info("Transaction for Account ID {} resulted in a Fraud Risk Score of {}.", account.getAccountId(), score);
        return score;
    }

    /**
     * Checks if the transaction risk is high enough to trigger an OTP requirement.
     */
    public boolean requiresOtp(int riskScore) {
        return riskScore >= HIGH_RISK_SCORE_THRESHOLD;
    }

    /**
     * Clears the velocity counter for the account (for simulation reset).
     * In a real system, this is time-based.
     */
    public void resetActivity(Long accountId) {
        recentActivity.remove(accountId);
        log.info("Fraud service activity counter reset for Account ID {}.", accountId);
    }
}