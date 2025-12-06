package com.hms.hotel.service;

import com.hms.hotel.domain.finance.ITaxStrategy;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * LLD: Composite Strategy Pattern - Context
 * Manages and applies multiple tax strategies to calculate the final tax amount.
 */
@Service
public class TaxCalculationService {

    private final Map<String, ITaxStrategy> availableStrategies;

    /**
     * Spring DI collects ALL ITaxStrategy implementations.
     */
    public TaxCalculationService(List<ITaxStrategy> strategies) {
        this.availableStrategies = strategies.stream()
                .collect(Collectors.toMap(ITaxStrategy::getName, strategy -> strategy));
    }

    /**
     * The core method: applies a list of specific taxes to the discounted rate.
     * @param discountedRatePerNight The price per night after discounts.
     * @param requiredTaxes A list of tax names (e.g., ["VAT", "CITY_FEE"]) to apply.
     * @return The total tax/fee amount per night.
     */
    public BigDecimal calculateTotalTaxPerNight(BigDecimal discountedRatePerNight, List<String> requiredTaxes) {
        BigDecimal totalTax = BigDecimal.ZERO;

        for (String taxName : requiredTaxes) {
            ITaxStrategy strategy = availableStrategies.get(taxName);

            if (strategy == null) {
                // Edge Case: Log error or throw exception if a required tax strategy is missing
                System.err.println("Warning: Missing tax strategy for name: " + taxName);
                continue;
            }

            // Execute the strategy and accumulate the tax
            BigDecimal taxAmount = strategy.calculateTax(discountedRatePerNight);
            totalTax = totalTax.add(taxAmount);
        }

        return totalTax;
    }
}