package com.amazon.paymentservice.infrastructure.adapter;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

// Concrete implementation of the ExternalGatewayAPI interface (Simulated Adaptee)
@Component
public class SimulatedStripeAPI implements ExternalGatewayAPI {

    @Override
    public String chargeAmount(BigDecimal amount, String token, String currency) {
        // Simulate real-world latency
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}

        // Simulation logic: fail if amount is negative
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid charge amount.");
        }

        // Return a unique gateway transaction ID
        return "str_tx_" + System.currentTimeMillis();
    }

    @Override
    public String refundTransaction(String transactionId) {
        // Simple simulation
        return "ref_tx_" + transactionId;
    }
}