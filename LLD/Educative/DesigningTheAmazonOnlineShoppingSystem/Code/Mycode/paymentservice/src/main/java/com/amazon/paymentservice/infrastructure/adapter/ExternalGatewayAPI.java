package com.amazon.paymentservice.infrastructure.adapter;

import java.math.BigDecimal;

// The interface of the third-party library we are integrating with.
// This is outside our control.
public interface ExternalGatewayAPI {

    // Vendor-specific method names and parameters
    String chargeAmount(BigDecimal amount, String token, String currency);
    String refundTransaction(String transactionId);
}