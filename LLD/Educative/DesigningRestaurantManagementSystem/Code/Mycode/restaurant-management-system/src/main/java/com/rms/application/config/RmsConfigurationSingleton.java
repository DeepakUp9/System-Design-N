package com.rms.application.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Singleton Pattern Implementation:
 * Ensures a single, globally accessible source for critical configuration data.
 * Spring ensures the Singleton scope via the @Component annotation by default.
 */
@Component
public class RmsConfigurationSingleton {

    private BigDecimal localTaxRate; // e.g., 5%
    private String currencyCode;
    private boolean seasonalSurchargeEnabled;

    @PostConstruct
    public void init() {
        // Production code would typically load these from a centralized source (DB, Vault, or application.yml)
        this.localTaxRate = new BigDecimal("0.05").setScale(4, RoundingMode.HALF_UP);
        this.currencyCode = "USD";
        this.seasonalSurchargeEnabled = true;

        System.out.println("\n[SINGLETON]: RMS Configuration initialized (Tax: 5%, Surcharge: ON)");
    }

    // --- Public Accessors ---

    public BigDecimal getLocalTaxRate() {
        return localTaxRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public boolean isSeasonalSurchargeEnabled() {
        return seasonalSurchargeEnabled;
    }

    // Global setter for enabling/disabling features in real-time
    public void setSeasonalSurchargeEnabled(boolean seasonalSurchargeEnabled) {
        this.seasonalSurchargeEnabled = seasonalSurchargeEnabled;
    }
}