package com.hms.hotel.domain.rate.concrete;

import com.hms.hotel.domain.rate.BookingDetails;
import com.hms.hotel.domain.rate.IRateStrategy;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * LLD: Concrete Strategy
 * Returns the base price without any discounts or adjustments.
 */
@Component("standardRateStrategy") // Use @Component for Spring DI
public class StandardRateStrategy implements IRateStrategy {

    @Override
    public String getStrategyName() {
        return "STANDARD";
    }

    @Override
    public BigDecimal calculateRate(BigDecimal basePrice, BookingDetails details) {
        // Base rate calculation logic is just the base price
        return basePrice;
    }
}