package com.airline.pricing.strategy.impl;

import com.airline.pricing.domain.PricingContext;
import com.airline.pricing.strategy.PricingStrategy;
import com.airline.reservation.domain.SeatClass;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BusinessClassStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculate(PricingContext context) {
        return context.getBasePrice().multiply(new BigDecimal("3.00")); // 3× price
    }

    @Override
    public boolean isApplicable(PricingContext context) {
        return context.getSeatClass() == SeatClass.BUSINESS;
    }
}
