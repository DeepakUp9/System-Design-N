package com.amazon.paymentservice.infrastructure.adapter;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class StripeGatewayAdapter {

    // Holds a reference to the actual third-party service
    private final ExternalGatewayAPI stripeApi;

    // Inject the simulated or real external API dependency
    public StripeGatewayAdapter(ExternalGatewayAPI stripeApi) {
        this.stripeApi = stripeApi;
    }

    /**
     * Our internal clean interface method used by PaymentStrategy.
     * It handles the mapping to the complex, vendor-specific chargeAmount.
     */
    public String charge(BigDecimal amount, String cardToken) {
        // LLD: Translation/Mapping Logic happens here

        // 1. Logging and preparation (e.g., currency conversion, security checks)
        String currencyCode = "USD";

        // 2. Delegate to the vendor's API
        String gatewayResponseId = stripeApi.chargeAmount(amount, cardToken, currencyCode);

        System.out.println("Adapter: Successfully charged " + amount + " via Stripe. ID: " + gatewayResponseId);

        // 3. Return the clean, useful result
        return gatewayResponseId;
    }

    // Similarly, a refund method would be implemented here.
}