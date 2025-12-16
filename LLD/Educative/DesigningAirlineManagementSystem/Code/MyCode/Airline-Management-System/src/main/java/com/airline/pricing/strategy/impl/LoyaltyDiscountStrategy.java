package com.airline.pricing.strategy.impl;

import com.airline.pricing.domain.PricingContext;
import com.airline.pricing.strategy.PricingStrategy;
import com.airline.reservation.domain.SeatClass;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LoyaltyDiscountStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculate(PricingContext context) {
        return context.getBasePrice().multiply(new BigDecimal("0.90")); // 10% off
    }

    @Override
    public boolean isApplicable(PricingContext context) {
        return context.isLoyaltyMember();
    }
}



