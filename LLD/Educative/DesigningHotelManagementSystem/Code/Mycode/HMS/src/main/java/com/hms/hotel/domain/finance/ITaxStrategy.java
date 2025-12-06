package com.hms.hotel.domain.finance;

import java.math.BigDecimal;

/**
 * LLD: Strategy Pattern - Tax Strategy Interface
 * Defines a single method for calculating tax/fees.
 */
public interface ITaxStrategy {

    String getName();

    /**
     * Calculates the additional cost (tax or fee) based on the current price.
     * @param price The base price *per night* after any discounts.
     * @return The calculated tax/fee amount per night.
     */
    BigDecimal calculateTax(BigDecimal price);
}