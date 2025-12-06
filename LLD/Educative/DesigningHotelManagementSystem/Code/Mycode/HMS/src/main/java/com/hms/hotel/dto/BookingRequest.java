package com.hms.hotel.dto;

import java.time.LocalDate;

/**
 * DTO: Data Transfer Object for creating a new booking.
 */
public record BookingRequest(
        String roomNumber,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        int numberOfGuests,
        boolean isLoyaltyMember,
        String corporateCode,
        String paymentMethod // Used by PaymentFactory
) {}