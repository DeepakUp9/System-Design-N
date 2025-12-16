package com.airline.pricing.strategy;

import com.airline.pricing.domain.PricingContext;
import java.math.BigDecimal;

/**
 * The Strategy Interface.
 * All concrete pricing algorithms must implement this.
 */
public interface PricingStrategy {
    BigDecimal calculate(PricingContext context);
    boolean isApplicable(PricingContext context); // Strategy selection logic
}