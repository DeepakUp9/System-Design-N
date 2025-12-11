package com.rms.core.strategy;

import java.math.BigDecimal;

/**
 * New Strategy Interface: Defines the method for calculating the final price.
 */
public interface PricingStrategy {

    String getType();
    BigDecimal calculateFinalPrice(BigDecimal basePrice);
}