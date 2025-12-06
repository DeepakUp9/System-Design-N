package com.hms.hotel.domain.finance.concrete;

import com.hms.hotel.domain.finance.ITaxStrategy;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * LLD: Concrete Tax Strategy - Fixed City Tourism Fee (Per Night)
 */
@Component("cityFeeStrategy")
public class CityTourismFeeStrategy implements ITaxStrategy {

    private static final BigDecimal CITY_FEE = new BigDecimal("5.00"); // $5.00 per night

    @Override
    public String getName() {
        return "CITY_FEE";
    }

    @Override
    public BigDecimal calculateTax(BigDecimal price) {
        // The fee is fixed, independent of the price
        return CITY_FEE;
    }
}