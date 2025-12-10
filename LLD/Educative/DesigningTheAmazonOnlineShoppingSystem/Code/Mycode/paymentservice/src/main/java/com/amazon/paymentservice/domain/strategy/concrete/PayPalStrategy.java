package com.amazon.paymentservice.domain.strategy.concrete;

import com.amazon.paymentservice.domain.model.PaymentRequest;
import com.amazon.paymentservice.domain.model.PaymentTransaction;
import com.amazon.paymentservice.domain.model.TransactionStatus;
import com.amazon.paymentservice.domain.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component("PAYPAL")
public class PayPalStrategy implements PaymentStrategy {

    @Override
    public String getPaymentMethodName() {
        return "PAYPAL";
    }

    @Override
    public PaymentTransaction processPayment(PaymentRequest request) {
        System.out.println("Processing PayPal payment for Order: " + request.getOrderId());

        String paypalEmail = request.getPaymentDetails().get("email");

        // Logic specific to PayPal API: redirect, confirmation, webhook wait, etc.
        if (paypalEmail != null && paypalEmail.contains("@")) {
            // For a demo, assume success
            String gatewayId = "PP-SUCCESS-" + System.currentTimeMillis();
            return createTransaction(request, TransactionStatus.SUCCESS, gatewayId);
        } else {
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