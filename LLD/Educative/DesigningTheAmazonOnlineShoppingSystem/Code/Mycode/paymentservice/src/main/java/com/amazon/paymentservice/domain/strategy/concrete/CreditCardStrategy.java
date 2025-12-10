package com.amazon.paymentservice.domain.strategy.concrete;

import com.amazon.paymentservice.domain.model.PaymentRequest;
import com.amazon.paymentservice.domain.model.PaymentTransaction;
import com.amazon.paymentservice.domain.model.TransactionStatus;
import com.amazon.paymentservice.domain.strategy.PaymentStrategy;
import com.amazon.paymentservice.infrastructure.adapter.StripeGatewayAdapter; // Hypothetical Adapter
import org.springframework.stereotype.Component;

@Component("CREDIT_CARD") // Spring Component name is the strategy identifier
public class CreditCardStrategy implements PaymentStrategy {

    private final StripeGatewayAdapter stripeAdapter; // Dependency on the Adapter

    // Spring DI for the Adapter
    public CreditCardStrategy(StripeGatewayAdapter stripeAdapter) {
        this.stripeAdapter = stripeAdapter;
    }

    @Override
    public String getPaymentMethodName() {
        return "CREDIT_CARD";
    }

    @Override
    public PaymentTransaction processPayment(PaymentRequest request) {
        System.out.println("Processing Credit Card payment for Order: " + request.getOrderId());

        // LLD: Strategy delegates the external call to the Adapter
        // The Adapter handles the vendor-specific API integration details
        String cardToken = request.getPaymentDetails().get("cardToken");

        // Resilience: Wrap external call in try-catch/circuit breaker
        try {
            String gatewayId = stripeAdapter.charge(request.getAmount(), cardToken);

            // Build and return the successful transaction record
            return createTransaction(request, TransactionStatus.SUCCESS, gatewayId);
        } catch (Exception e) {
            // Production Standard: Detailed logging and error status
            System.err.println("Credit Card payment failed: " + e.getMessage());
            return createTransaction(request, TransactionStatus.FAILED, null);
        }
    }

    // Helper method for transaction creation (omitted details)
    private PaymentTransaction createTransaction(PaymentRequest request, TransactionStatus status, String gatewayId) {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrderId(request.getOrderId());
        tx.setAmount(request.getAmount());
        tx.setPaymentMethod(getPaymentMethodName());
        tx.setStatus(status);
        tx.setGatewayTransactionId(gatewayId);
        return tx;
    }
}