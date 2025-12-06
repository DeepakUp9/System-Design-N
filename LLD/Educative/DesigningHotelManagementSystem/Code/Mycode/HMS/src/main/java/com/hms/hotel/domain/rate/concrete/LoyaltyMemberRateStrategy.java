package com.hms.hotel.domain.rate.concrete;

import com.hms.hotel.domain.rate.BookingDetails;
import com.hms.hotel.domain.rate.IRateStrategy;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * LLD: Concrete Strategy
 * Applies a fixed discount if the booking is tied to a loyalty member.
 */
@Component("loyaltyRateStrategy")
public class LoyaltyMemberRateStrategy implements IRateStrategy {

    private static final BigDecimal LOYALTY_DISCOUNT_FACTOR = new BigDecimal("0.90"); // 10% discount

    @Override
    public String getStrategyName() {
        return "LOYALTY";
    }

    @Override
    public BigDecimal calculateRate(BigDecimal basePrice, BookingDetails details) {
        if (details.isLoyaltyMember()) {
            return basePrice.multiply(LOYALTY_DISCOUNT_FACTOR)
                    .setScale(2, RoundingMode.HALF_UP);
        }
        // Fallback to standard price if the condition isn't met (though in a real system, the context would select the standard rate instead)
        return basePrice;
    }
}