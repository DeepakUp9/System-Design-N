package com.airline.pricing.strategy.impl;

import com.airline.pricing.domain.PricingContext;
import com.airline.pricing.strategy.PricingStrategy;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
public class LastMinuteStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculate(PricingContext context) {
        return context.getBasePrice().multiply(new BigDecimal("1.25"));
    }

    @Override
    public boolean isApplicable(PricingContext context) {
        long hoursToDeparture = ChronoUnit.HOURS.between(context.getBookingTime(), context.getDepartureTime());
        return hoursToDeparture < 48 && hoursToDeparture > 0;
    }
}