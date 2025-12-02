package com.atm.machine.service;

import com.atm.machine.dto.TransactionRequest;
import com.atm.machine.entity.Account;
import com.atm.machine.entity.LimitType;
import com.atm.machine.exception.CardAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service to enforce daily transaction limits for withdrawals and transfers.
 * NOTE: In a real system, this state MUST be stored in a persistent, high-speed
 * data store like Redis or a dedicated database table for shared, distributed tracking.
 * This in-memory version is for demonstration purposes only.
 */
@Service
@Slf4j
public class LimitService {

    // Key: Account ID (Long)
    // Value: Map<LocalDate, AtomicReference<BigDecimal>> (Tracks daily total)
    private final ConcurrentHashMap<Long, ConcurrentHashMap<LocalDate, AtomicReference<BigDecimal>>> dailyLimitsTracker = new ConcurrentHashMap<>();

    // Business rule: Maximum daily withdrawal amount, regardless of currency (assuming conversion later)
    private static final BigDecimal DAILY_WITHDRAWAL_LIMIT = new BigDecimal("1000.00");
    private static final BigDecimal DAILY_TRANSFER_LIMIT = new BigDecimal("5000.00");
    private static final int MAX_WITHDRAWALS_PER_DAY = 5;

    /**
     * Checks if the proposed transaction exceeds the daily limits.
     * If successful, it updates the tracker.
     *
     * @param account The account making the transaction.
     * @param request The transaction details.
     * @param type The type of limit to check (WITHDRAWAL or TRANSFER).
     */
    public void enforceAndRecordLimit(Account account, TransactionRequest request, LimitType type) {
        LocalDate today = LocalDate.now();

        // Retrieve or initialize the daily tracker for the account
        dailyLimitsTracker.putIfAbsent(account.getAccountId(), new ConcurrentHashMap<>());
        ConcurrentHashMap<LocalDate, AtomicReference<BigDecimal>> accountTracker = dailyLimitsTracker.get(account.getAccountId());

        // Retrieve or initialize the total amount processed today
        accountTracker.putIfAbsent(today, new AtomicReference<>(BigDecimal.ZERO));
        AtomicReference<BigDecimal> dailyTotalRef = accountTracker.get(today);

        BigDecimal limit;
        String limitName;

        if (type == LimitType.WITHDRAWAL) {
            limit = DAILY_WITHDRAWAL_LIMIT;
            limitName = "Withdrawal";
            // Simple request count check (not robust, but demonstrates the concept)
            if (dailyTotalRef.get().intValue() > MAX_WITHDRAWALS_PER_DAY * limit.intValue()) {
                // Simple request count check is hard to implement with just amount,
                // but let's focus on the amount for clarity.
            }
        } else if (type == LimitType.TRANSFER) {
            limit = DAILY_TRANSFER_LIMIT;
            limitName = "Transfer";
        } else {
            return; // No limit check needed for deposits/inquiries
        }

        // Calculate the new total if the transaction is allowed
        BigDecimal newDailyTotal = dailyTotalRef.get().add(request.getAmount());

        if (newDailyTotal.compareTo(limit) > 0) {
            log.warn("Limit exceeded for Account ID {}. Type: {}. Current total: {}. Proposed: {}. Limit: {}",
                    account.getAccountId(), limitName, dailyTotalRef.get(), request.getAmount(), limit);
            throw new CardAuthenticationException(String.format("Daily %s limit of %s %s exceeded.",
                    limitName, limit.toString(), account.getCurrencyCode()));
        }

        // Record the transaction amount by atomically updating the total
        dailyTotalRef.updateAndGet(current -> current.add(request.getAmount()));
        log.info("Limit check passed for Account ID {}. Daily {} total updated to {}.",
                account.getAccountId(), limitName, dailyTotalRef.get());
    }

    /**
     * Resets daily usage limits for an account.
     * This should be called:
     * - After successful transactions to reset if needed
     * - By a scheduled job at midnight daily
     * - When unlocking an account after being blocked
     */
    public void resetDailyLimits(Long accountId) {
        LocalDate today = LocalDate.now();

        // Get the account's tracker
        ConcurrentHashMap<LocalDate, AtomicReference<BigDecimal>> accountTracker =
                dailyLimitsTracker.get(accountId);

        if (accountTracker != null) {
            // Remove yesterday's data to free memory
            LocalDate yesterday = today.minusDays(1);
            accountTracker.remove(yesterday);

            // Reset today's total to zero
            accountTracker.put(today, new AtomicReference<>(BigDecimal.ZERO));

            log.info("Daily limits reset for account ID: {}. Date: {}", accountId, today);
        } else {
            // Initialize tracker for this account if it doesn't exist
            dailyLimitsTracker.putIfAbsent(accountId, new ConcurrentHashMap<>());
            dailyLimitsTracker.get(accountId).put(today, new AtomicReference<>(BigDecimal.ZERO));

            log.info("Daily limits initialized for account ID: {}. Date: {}", accountId, today);
        }
    }

    /**
     * Gets the current daily usage for an account
     */
    public BigDecimal getDailyUsage(Long accountId, LimitType type) {
        LocalDate today = LocalDate.now();
        ConcurrentHashMap<LocalDate, AtomicReference<BigDecimal>> accountTracker =
                dailyLimitsTracker.get(accountId);

        if (accountTracker != null) {
            AtomicReference<BigDecimal> dailyTotalRef = accountTracker.get(today);
            return dailyTotalRef != null ? dailyTotalRef.get() : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    /**
     * Scheduled method to reset all limits at midnight (optional)
     */
    // @Scheduled(cron = "0 0 0 * * ?") // Uncomment to run daily at midnight
    public void resetAllDailyLimits() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        dailyLimitsTracker.forEach((accountId, tracker) -> {
            tracker.remove(yesterday); // Clean up old data
            tracker.put(today, new AtomicReference<>(BigDecimal.ZERO)); // Reset today
        });

        log.info("Reset daily limits for all accounts. Date: {}", today);
    }

}