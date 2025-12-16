package com.airline.pricing.service;

import com.airline.pricing.domain.PricingContext;
import com.airline.pricing.strategy.PricingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {

    // Spring injects all implementations of PricingStrategy into this list
    private final List<PricingStrategy> pricingStrategies;

    public BigDecimal calculateFinalPrice(PricingContext context) {
        // Find the first applicable strategy, or fall back to a default
        return pricingStrategies.stream()
                .filter(strategy -> strategy.isApplicable(context))
                .findFirst()
                .map(strategy -> strategy.calculate(context))
                .orElse(context.getBasePrice()); // Default: Standard Price
    }

    @Cacheable(value = "flightPrices", key = "#context.flightId + #context.seatClass")
    public BigDecimal getCachedPrice(PricingContext context) {
        return calculateFinalPrice(context);
    }
}