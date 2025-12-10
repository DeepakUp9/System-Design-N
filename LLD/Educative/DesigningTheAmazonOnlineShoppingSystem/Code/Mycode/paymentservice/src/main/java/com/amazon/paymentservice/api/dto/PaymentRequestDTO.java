package com.amazon.paymentservice.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

public record PaymentRequestDTO(
        @NotNull(message = "Order ID is mandatory for transaction tracking")
        Long orderId,

        @NotNull(message = "Amount is mandatory")
        @DecimalMin(value = "0.01", message = "Amount must be positive")
        BigDecimal amount,

        @NotBlank(message = "Payment method must be specified")
        String paymentMethod, // Maps directly to our Strategy name (e.g., CREDIT_CARD, PAYPAL)

        @NotNull(message = "Payment details are required")
        // Contains the polymorphic details: card token, email, etc.
        Map<String, String> paymentDetails
) {}