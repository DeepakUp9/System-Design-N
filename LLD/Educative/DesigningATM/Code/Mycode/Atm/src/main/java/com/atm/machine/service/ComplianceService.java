package com.atm.machine.service;

import com.atm.machine.entity.Customer;
import com.atm.machine.entity.KycStatus;
import com.atm.machine.entity.Transaction;
import com.atm.machine.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service managing KYC status and generating mock regulatory reports (AML/SAR).
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ComplianceService {

    private final TransactionRepository transactionRepository;

    private static final BigDecimal SAR_THRESHOLD = new BigDecimal("9000.00"); // Threshold for Suspicious Activity Report (SAR)

    /**
     * Checks if a customer's KYC status is verified.
     * @param customer The customer to check.
     * @return True if verified, false otherwise.
     */
    public boolean isKycVerified(Customer customer) {
        return customer.getKycStatus() == KycStatus.VERIFIED;
    }

    /**
     * Simulates the daily generation of a Suspicious Activity Report (SAR)
     * based on transactions exceeding a threshold (AML requirement).
     * NOTE: This is a simplified, non-persistent simulation.
     */
    public String generateSuspiciousActivityReport() {
        ZonedDateTime today = ZonedDateTime.now().minusDays(1);
        ZonedDateTime yesterday = today.minusDays(1);

        List<Transaction> highValueTransactions = transactionRepository
                .findHighValueTransactionsBetween(yesterday, today, SAR_THRESHOLD);

        if (highValueTransactions.isEmpty()) {
            return "Regulatory Report: SAR for " + today.toLocalDate() + " generated. No suspicious activity found.";
        }

        AtomicInteger reportCount = new AtomicInteger(0);

        String report = highValueTransactions.stream()
                .map(tx -> {
                    reportCount.incrementAndGet();
                    return String.format("\n[CASE %d] Account: %s, Amount: %s %s, Type: %s, Timestamp: %s",
                            reportCount.get(),
                            tx.getAccount().getAccountNumber(),
                            tx.getAmount().toString(),
                            tx.getAccount().getCurrencyCode(),
                            tx.getTransactionType().name(),
                            tx.getTransactionTimestamp()
                    );
                })
                .collect(
                        () -> new StringBuilder("Regulatory Report: SAR generated for " + today.toLocalDate() + ".\nTotal suspicious transactions exceeding $" + SAR_THRESHOLD + ": " + highValueTransactions.size()),
                        StringBuilder::append,
                        StringBuilder::append
                ).toString();

        log.warn("--- SAR GENERATED --- {}", report);
        return report;
    }
}
