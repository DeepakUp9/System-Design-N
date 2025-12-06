package com.hms.hotel.domain.finance.concrete;

import com.hms.hotel.domain.finance.ITaxStrategy;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * LLD: Concrete Tax Strategy - 15% VAT (Example for a specific region)
 */
@Component("vatTaxStrategy")
public class ValueAddedTaxStrategy implements ITaxStrategy {

    private static final BigDecimal VAT_RATE = new BigDecimal("0.15");

    @Override
    public String getName() {
        return "VAT";
    }

    @Override
    public BigDecimal calculateTax(BigDecimal price) {
        // Calculate 15% of the price
        return price.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}