package com.rms.core.strategy;

import com.rms.application.config.RmsConfigurationSingleton;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Concrete Strategy: Calculates price using a flat 10% discount.
 */
@Component("promotionalPricingStrategy")
public class PromotionalPricingStrategy implements PricingStrategy {

    private final RmsConfigurationSingleton config;
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.90"); // 10% discount

    public PromotionalPricingStrategy(RmsConfigurationSingleton config) {
        this.config = config;
    }

    @Override
    public String getType() {
        return "PROMO_10";
    }

    @Override
    public BigDecimal calculateFinalPrice(BigDecimal basePrice) {
        // 1. Apply Discount
        BigDecimal discountedPrice = basePrice.multiply(DISCOUNT_RATE);

        // 2. Apply Local Tax Rate (From the Singleton)
        BigDecimal finalPrice = discountedPrice.multiply(config.getLocalTaxRate().add(BigDecimal.ONE));

        System.out.println("Pricing: Applied 10% Promotional Discount.");

        return finalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}