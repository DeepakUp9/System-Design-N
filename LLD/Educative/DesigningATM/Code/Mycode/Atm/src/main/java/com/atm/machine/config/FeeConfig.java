package com.atm.machine.config;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Configuration component to hold and manage all standard banking fees.
 * In a production system, these would be loaded from a dynamic configuration store or DB.
 */
@Component
public class FeeConfig {

    // Fee applied for any withdrawal transaction (flat fee)
    public static final BigDecimal ATM_WITHDRAWAL_FLAT_FEE = new BigDecimal("2.50");

    // Fee applied for transfers (percentage fee)
    public static final BigDecimal TRANSFER_PERCENTAGE_FEE = new BigDecimal("0.005"); // 0.5%

    // Overdraft policies
    public static final BigDecimal OVERDRAFT_CHARGE_FLAT_FEE = new BigDecimal("35.00");
    public static final BigDecimal OVERDRAFT_LINE_OF_CREDIT = new BigDecimal("500.00"); // Max negative balance allowed

    // Currency specific fee overrides (e.g., EUR accounts have a higher local ATM fee)
    private static final Map<String, BigDecimal> ATM_FEE_OVERRIDES = Map.of(
            "EUR", new BigDecimal("3.00"),
            "GBP", new BigDecimal("4.00")
    );

    /**
     * Retrieves the applicable ATM Withdrawal fee for a given account currency.
     */
    public BigDecimal getAtmWithdrawalFee(String currencyCode) {
        return ATM_FEE_OVERRIDES.getOrDefault(currencyCode, ATM_WITHDRAWAL_FLAT_FEE);
    }
}
