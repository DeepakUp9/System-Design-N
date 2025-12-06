package com.hms.hotel.domain.rate;

import java.time.LocalDate;

// Note: This is a simple POJO, not a JPA Entity.
public record BookingDetails(
        String roomType,
        int numberOfNights,
        boolean isLoyaltyMember,
        String corporateCode,
        LocalDate checkInDate,
        LocalDate checkOutDate
) {
    // A simple, immutable data holder for the Strategy Pattern context.
}