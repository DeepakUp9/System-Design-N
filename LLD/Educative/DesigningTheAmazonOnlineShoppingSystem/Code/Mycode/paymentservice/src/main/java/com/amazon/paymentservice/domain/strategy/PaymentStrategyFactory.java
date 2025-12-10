package com.amazon.paymentservice.domain.strategy;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

// Factory Pattern: Manages retrieval of the correct Strategy bean
@Component
public class PaymentStrategyFactory {

    // Spring injects all beans of the specified type (PaymentStrategy) into this map
    private final ApplicationContext applicationContext;

    public PaymentStrategyFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * LLD Strategy Lookup: Retrieves the correct PaymentStrategy implementation.
     * @param paymentMethodName The name of the strategy (e.g., "CREDIT_CARD")
     * @return The specific PaymentStrategy implementation
     * @throws IllegalArgumentException if the strategy is not found
     */
    public PaymentStrategy getStrategy(String paymentMethodName) {
        // Production Standard: Use a consistent naming convention (upper case)
        String beanName = paymentMethodName.toUpperCase();

        if (!applicationContext.containsBean(beanName)) {
            // Resilience/Edge Case: Strategy not supported
            throw new IllegalArgumentException("Payment method not supported: " + paymentMethodName);
        }

        // Uses the ApplicationContext to get the bean instance
        return (PaymentStrategy) applicationContext.getBean(beanName, PaymentStrategy.class);
    }
}