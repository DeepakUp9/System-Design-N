package com.airline.pricing.strategy.impl;

import com.airline.pricing.domain.PricingContext;
import com.airline.pricing.strategy.PricingStrategy;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class HolidaySurgeStrategy implements PricingStrategy {
    private static final BigDecimal SURGE_MULTIPLIER = new BigDecimal("1.50");

    @Override
    public BigDecimal calculate(PricingContext context) {
        return context.getBasePrice().multiply(SURGE_MULTIPLIER);
    }

    @Override
    public boolean isApplicable(PricingContext context) {
        return context.isHolidaySeason();
    }
}