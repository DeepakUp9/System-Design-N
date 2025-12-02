package com.atm.machine.config;


import com.atm.machine.security.ApiKeyInterceptor;
import com.atm.machine.security.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to register interceptors.
 * Interceptors run BEFORE the request hits the Controller, allowing for security
 * and pre-processing checks (like API key and rate limiting).
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;
    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 1. API Key Check FIRST (Crucial: Protects against unauthorized clients)
        // All endpoints must pass this check.
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/v1/**");

        // 2. Rate Limiting Check SECOND (Protects against brute force/DDoS)
        // All endpoints must pass this check.
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/v1/**");
    }
}