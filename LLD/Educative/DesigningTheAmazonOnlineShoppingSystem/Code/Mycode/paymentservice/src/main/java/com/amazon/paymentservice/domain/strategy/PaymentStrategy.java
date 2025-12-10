package com.amazon.paymentservice.domain.strategy;

import com.amazon.paymentservice.domain.model.PaymentRequest;
import com.amazon.paymentservice.domain.model.PaymentTransaction;

// Strategy Interface: Defines the common method for processing payment
public interface PaymentStrategy {

    // Must return a unique identifier for Spring DI and persistence lookup
    String getPaymentMethodName();

    // The core operation: processes the payment and returns the resulting transaction record
    PaymentTransaction processPayment(PaymentRequest request);
}
