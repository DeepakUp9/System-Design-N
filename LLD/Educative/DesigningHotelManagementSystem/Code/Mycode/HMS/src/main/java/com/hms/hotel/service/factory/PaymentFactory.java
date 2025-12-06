package com.hms.hotel.service.factory;

import com.hms.hotel.entity.Booking;
import com.hms.hotel.entity.Payment;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * LLD: Simple Factory Pattern
 * Centralizes the creation logic for Payment objects.
 */
@Component
public class PaymentFactory {

    /**
     * Factory method to create a new Payment instance.
     * @param amount The payment amount.
     * @param method The method used (e.g., "CREDIT_CARD", "PAYPAL").
     * @param booking The associated booking.
     * @return A newly created and initialized Payment object.
     */
    public Payment createPayment(BigDecimal amount, String method, Booking booking) {

        // Factory logic: could involve specific initialization based on method

        // Edge Case: Payment method validation
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be null or empty.");
        }

        // The Factory creates and returns the object
        return new Payment(amount, method.toUpperCase(), booking);
    }
}