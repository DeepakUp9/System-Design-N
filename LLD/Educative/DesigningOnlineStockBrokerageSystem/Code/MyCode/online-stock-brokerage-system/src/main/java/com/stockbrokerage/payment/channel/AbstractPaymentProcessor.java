package com.stockbrokerage.payment.channel;

import com.stockbrokerage.order.exceptions.OrderProcessingException;
import java.math.BigDecimal;

/**
 * Template Method Pattern: Defines the strict algorithm for processing a payment.
 */
public abstract class AbstractPaymentProcessor {

    /**
     * The Template Method: Final and cannot be overridden, guaranteeing the sequence.
     * @param amount The amount to process.
     * @param accountId The target account.
     * @return A transaction ID.
     */
    public final String processPayment(BigDecimal amount, Long accountId) {

        // Step 1: Mandatory pre-check (Hook Method)
        if (!preValidate(amount, accountId)) {
            throw new OrderProcessingException("Payment pre-validation failed for account " + accountId);
        }

        // Step 2: Implementation specific logic (Abstract Primitive Operation)
        String transactionId = doTransfer(amount, accountId);

        // Step 3: Mandatory post-update
        updateAccountBalance(accountId, amount);

        // Step 4: Optional logging (Hook Method)
        if (shouldLogTransaction()) {
            logTransaction(transactionId, amount, accountId);
        }

        return transactionId;
    }

    // --- Primitive Abstract Operations (MUST be implemented by subclasses) ---
    protected abstract String doTransfer(BigDecimal amount, Long accountId);
    protected abstract void updateAccountBalance(Long accountId, BigDecimal amount);


    // --- Hook Methods (Can be overridden, optional) ---
    protected boolean preValidate(BigDecimal amount, Long accountId) {
        // Default Hook: Simple check. Subclasses can add specific KYC/AML checks.
        System.out.println("[TEMPLATE HOOK] Performing generic payment validation.");
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    protected boolean shouldLogTransaction() {
        // Default Hook: Always log unless overridden.
        return true;
    }

    protected void logTransaction(String transactionId, BigDecimal amount, Long accountId) {
        System.out.printf("[TEMPLATE LOG] Payment %s processed successfully. Amount: %s%n",
                transactionId, amount);
    }
}