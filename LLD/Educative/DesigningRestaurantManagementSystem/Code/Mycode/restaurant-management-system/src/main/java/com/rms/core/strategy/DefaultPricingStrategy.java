package com.rms.core.strategy;

import com.rms.application.config.RmsConfigurationSingleton;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Concrete Strategy: Calculates price using global tax and seasonal surcharge.
 */
@Component("defaultPricingStrategy")
public class DefaultPricingStrategy implements PricingStrategy {

    private final RmsConfigurationSingleton config;
    private static final BigDecimal SURCHARGE_RATE = new BigDecimal("1.05"); // 5% surcharge

    // Inject the Singleton instance
    public DefaultPricingStrategy(RmsConfigurationSingleton config) {
        this.config = config;
    }

    @Override
    public String getType() {
        return "DEFAULT";
    }

    @Override
    public BigDecimal calculateFinalPrice(BigDecimal basePrice) {
        BigDecimal finalPrice = basePrice;

        // 1. Apply Seasonal Surcharge (Edge Case/Toggleable Feature)
        if (config.isSeasonalSurchargeEnabled()) {
            finalPrice = finalPrice.multiply(SURCHARGE_RATE);
            System.out.println("Pricing: Applied 5% Seasonal Surcharge.");
        }

        // 2. Apply Local Tax Rate (From the Singleton)
        finalPrice = finalPrice.multiply(config.getLocalTaxRate().add(BigDecimal.ONE));

        // 3. Final Rounding
        return finalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}