package com.hms.hotel.domain.rate;

import java.math.BigDecimal;

/**
 * LLD: Strategy Pattern - Strategy Interface
 * Defines the contract for all rate calculation algorithms.
 */
public interface IRateStrategy {

    /**
     * Unique name of the strategy for lookup/DI.
     */
    String getStrategyName();

    /**
     * Calculates the final rate based on provided booking details.
     * @param basePrice The standard published room price.
     * @param details The BookingDetails containing contextual information.
     * @return The final calculated price per night.
     */
    BigDecimal calculateRate(BigDecimal basePrice, BookingDetails details);
}