package com.amazon.paymentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
// ... (imports for UserDetails, etc., similar to CatalogService)

@Configuration
@EnableMethodSecurity // CRITICAL: This enables @PreAuthorize
public class PaymentSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for Stateless REST APIs
                .authorizeHttpRequests(auth -> auth
                        // Authorization Rules
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/orders/*/pay").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/orders/*/ship").hasRole("ADMIN") // Role-check for shipping
                        .anyRequest().authenticated()
                )
                .httpBasic(); // Placeholder authentication

        return http.build();
    }

    // You also need the UserDetailsService bean for the placeholder authentication,
    // just as in the Catalog Service.
}