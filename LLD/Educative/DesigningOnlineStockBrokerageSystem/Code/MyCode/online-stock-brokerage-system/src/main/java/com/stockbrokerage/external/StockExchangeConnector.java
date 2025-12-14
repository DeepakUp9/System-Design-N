package com.stockbrokerage.external;

import org.springframework.stereotype.Service;

/**
 * Singleton Pattern: Ensures that only one instance of the StockExchange connection
 * (a costly or sensitive resource) is available globally.
 */
@Service
public final class StockExchangeConnector {

    // The static, private instance variable
    private static volatile StockExchangeConnector instance;
    private final String connectionId;

    // Private constructor prevents direct instantiation
    private StockExchangeConnector() {
        this.connectionId = "SEC-" + System.currentTimeMillis();
        System.out.println("Singleton: Initialized StockExchangeConnector with ID: " + this.connectionId);
        // Production: Initialize actual socket connections, API keys, etc.
    }

    /**
     * Public static method for global access to the single instance (Thread-safe).
     * @return The single StockExchangeConnector instance.
     */
    public static StockExchangeConnector getInstance() {
        if (instance == null) {
            synchronized (StockExchangeConnector.class) {
                if (instance == null) {
                    instance = new StockExchangeConnector();
                }
            }
        }
        return instance;
    }

    // Alternative for Spring usage: Inject via constructor and rely on Spring's default Singleton scope
    // The LLD focus here is the conceptual pattern.

    /**
     * The core business method.
     */
    public boolean sendOrderToMarket(String symbol, double quantity) {
        System.out.println("--- [EXCHANGE] --- Sending order via connection " + this.connectionId);
        // Real-world API call and response handling would be here
        return true;
    }
}