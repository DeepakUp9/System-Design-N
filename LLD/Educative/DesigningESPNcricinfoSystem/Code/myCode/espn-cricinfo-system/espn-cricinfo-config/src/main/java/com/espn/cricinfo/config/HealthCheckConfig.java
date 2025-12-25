package com.espn.cricinfo.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Health check configuration for monitoring application components.
 * Provides custom health indicators for Redis and other services.
 */
@Configuration
public class HealthCheckConfig {

    /**
     * Redis health indicator for monitoring Redis connectivity.
     */
    @Bean
    public HealthIndicator redisHealthIndicator(RedisTemplate<String, Object> redisTemplate) {
        return () -> {
            try {
                redisTemplate.getConnectionFactory().getConnection().ping();
                return Health.up()
                        .withDetail("redis", "Available")
                        .build();
            } catch (Exception e) {
                return Health.down()
                        .withDetail("redis", "Unavailable")
                        .withException(e)
                        .build();
            }
        };
    }

    /**
     * Application-specific health indicator.
     */
    @Bean
    public HealthIndicator applicationHealthIndicator() {
        return () -> Health.up()
                .withDetail("application", "ESPN Cricinfo System")
                .withDetail("version", "1.0.0-SNAPSHOT")
                .withDetail("status", "Running")
                .build();
    }
}
