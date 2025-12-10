package com.amazon.paymentservice.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

// Not a JPA entity, just a domain object passed between layers
@Data
public class PaymentRequest {

    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;

    // Map to hold polymorphic details: card number, PayPal email, crypto wallet address, etc.
    private Map<String, String> paymentDetails;

    // Getters and Setters Omitted
}
