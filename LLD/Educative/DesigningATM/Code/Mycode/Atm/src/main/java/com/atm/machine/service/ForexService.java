package com.atm.machine.service;

import com.atm.machine.exception.AtmException;
import com.atm.machine.exception.ForexException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Service to handle Foreign Exchange (Forex) calculations.
 * In a production system, this would call a secure, third-party FX provider API.
 * Here, it is simulated with hardcoded rates.
 * [Image of a currency exchange rate display]
 */
@Service
@Slf4j
public class ForexService {

    // Simulated exchange rates based on USD (Base Currency)
    // Key: Currency Code (e.g., "EUR"), Value: Exchange rate to USD (USD/EUR)
    private static final Map<String, BigDecimal> MOCK_RATES = Map.of(
            "USD", BigDecimal.valueOf(1.0000),
            "EUR", BigDecimal.valueOf(1.0800), // 1 EUR = 1.0800 USD
            "GBP", BigDecimal.valueOf(1.2500), // 1 GBP = 1.2500 USD
            "JPY", BigDecimal.valueOf(0.0065)  // 1 JPY = 0.0065 USD
    );

    // Fee applied as a percentage on top of the transaction for cross-currency conversion
    private static final BigDecimal FX_CONVERSION_MARGIN = new BigDecimal("0.035"); // 3.5% fee on conversion

    /**
     * Converts a source amount from one currency to another.
     * Conversion always goes via the common base (USD in this case).
     * Formula: amountInSource * (SourceRate / TargetRate) * (1 - FX_CONVERSION_MARGIN)
     *
     * @param sourceCurrency The currency of the amount provided.
     * @param targetCurrency The currency to convert to.
     * @param amount The amount in the source currency.
     * @return The converted amount in the target currency, minus the FX fee.
     */
    public BigDecimal convert(String sourceCurrency, String targetCurrency, BigDecimal amount) {

        if (sourceCurrency.equals(targetCurrency)) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal sourceRate = MOCK_RATES.get(sourceCurrency);
        BigDecimal targetRate = MOCK_RATES.get(targetCurrency);

        if (sourceRate == null || targetRate == null) {
            log.error("Unsupported currency conversion requested: {} to {}", sourceCurrency, targetCurrency);
            throw new ForexException("Unsupported currency: " + sourceCurrency + " or " + targetCurrency);
        }

        // 1. Calculate the raw conversion factor
        // Amount in Target = Amount in Source * (SourceRate / TargetRate)
        BigDecimal rawConversionFactor = sourceRate.divide(targetRate, 8, RoundingMode.HALF_UP);
        BigDecimal amountInTarget = amount.multiply(rawConversionFactor).setScale(8, RoundingMode.HALF_UP);

        // 2. Calculate the FX Fee (3.5% of the converted amount)
        BigDecimal fxFee = amountInTarget.multiply(FX_CONVERSION_MARGIN).setScale(4, RoundingMode.HALF_UP);

        // 3. Apply Fee and get final amount
        BigDecimal netAmountInTarget = amountInTarget.subtract(fxFee).setScale(2, RoundingMode.HALF_UP);

        log.info("FX Conversion: {} {} -> {} {}. Fee applied: {}",
                amount.setScale(2, RoundingMode.HALF_UP), sourceCurrency,
                netAmountInTarget, targetCurrency, fxFee);

        return netAmountInTarget;
    }

    /**
     * Calculates the FX Fee amount (in the source currency) for reporting.
     */
    public BigDecimal calculateFeeInSource(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        if (sourceCurrency.equals(targetCurrency)) {
            return BigDecimal.ZERO;
        }

        // This is complex. For simplicity, we calculate the equivalent of the 3.5% fee
        // in the source currency by converting the target currency fee back.
        // In a real system, the fee is usually based on the transaction amount before conversion.

        BigDecimal sourceRate = MOCK_RATES.get(sourceCurrency);
        BigDecimal targetRate = MOCK_RATES.get(targetCurrency);

        if (sourceRate == null || targetRate == null) {
            //return BigDecimal.ZERO;
            throw new ForexException("Unsupported currency: " + sourceCurrency + " or " + targetCurrency);
        }

        BigDecimal rawConversionFactor = sourceRate.divide(targetRate, 8, RoundingMode.HALF_UP);
        BigDecimal amountInTarget = amount.multiply(rawConversionFactor).setScale(8, RoundingMode.HALF_UP);

        BigDecimal fxFeeInTarget = amountInTarget.multiply(FX_CONVERSION_MARGIN);

        // Convert the fee back to the source currency for reporting/record keeping
        // FeeInSource = FeeInTarget * (TargetRate / SourceRate)
        BigDecimal feeConversionFactor = targetRate.divide(sourceRate, 8, RoundingMode.HALF_UP);

        return fxFeeInTarget.multiply(feeConversionFactor).setScale(2, RoundingMode.HALF_UP);
    }
}