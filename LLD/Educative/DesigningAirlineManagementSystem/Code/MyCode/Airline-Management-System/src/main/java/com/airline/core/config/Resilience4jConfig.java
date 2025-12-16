package com.airline.core.config;


import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;

// ✅ CORRECT - This is Resilience4j annotation

@Configuration
public class Resilience4jConfig {

    // This bean is auto-created by Spring Boot
    // But you can customize it here if needed

    // Example: Custom Event Consumers for logging
    @Bean
    public Customizer<CircuitBreakerConfigCustomizer> circuitBreakerCustomizer() {
        return customizer -> customizer
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .onStateTransition(event -> {
                    log.info("Circuit Breaker {} changed state from {} to {}",
                            event.getCircuitBreakerName(),
                            event.getStateTransition().getFromState(),
                            event.getStateTransition().getToState());
                });
    }
}
