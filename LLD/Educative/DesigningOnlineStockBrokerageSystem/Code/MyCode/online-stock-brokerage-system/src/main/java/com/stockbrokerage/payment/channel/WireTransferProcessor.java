package com.stockbrokerage.payment.channel;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Concrete Template 1: Implements the abstract steps for Wire Transfer payments.
 */
@Service
public class WireTransferProcessor extends AbstractPaymentProcessor {

    @Override
    protected String doTransfer(BigDecimal amount, Long accountId) {
        String txId = "WIRE-" + System.currentTimeMillis();
        System.out.printf("[PRIMITIVE] Initiating real-time Wire Transfer of %s for account %d.%n", amount, accountId);
        // Production: Call external banking API
        return txId;
    }

    @Override
    protected void updateAccountBalance(Long accountId, BigDecimal amount) {
        System.out.printf("[PRIMITIVE] Crediting internal balance of account %d by %s (Wire).%n", accountId, amount);
        // Production: Call AccountService to finalize balance update
    }

    // Overriding the preValidate hook for specific AML/KYC checks
    @Override
    protected boolean preValidate(BigDecimal amount, Long accountId) {
        boolean genericValid = super.preValidate(amount, accountId);
        // Additional Wire-specific check (e.g., must be over $1000)
        boolean wireCheck = amount.compareTo(new BigDecimal("1000")) >= 0;

        if (!wireCheck) {
            System.err.println("[WIRE HOOK] Wire minimum amount check failed.");
        }
        return genericValid && wireCheck;
    }
}