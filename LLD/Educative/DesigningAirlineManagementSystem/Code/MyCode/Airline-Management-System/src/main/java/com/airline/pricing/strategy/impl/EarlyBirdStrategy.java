package com.airline.pricing.strategy.impl;

import com.airline.pricing.domain.PricingContext;
import com.airline.pricing.strategy.PricingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
public class EarlyBirdStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculate(PricingContext context) {
        return context.getBasePrice().multiply(new BigDecimal("0.85")); // 15% off
    }

    @Override
    public boolean isApplicable(PricingContext context) {
        long daysToDeparture = ChronoUnit.DAYS.between(
                context.getBookingTime(),
                context.getDepartureTime()
        );
        return daysToDeparture > 30; // Booked >30 days in advance
    }
}
