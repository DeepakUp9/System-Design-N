package com.atm.machine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class to provide necessary security beans.
 * NOTE: We are intentionally *not* configuring HTTP security here,
 * as the ATM application logic will enforce security checks (card/pin)
 * directly in the service layer, separate from standard web authentication.
 */
@Configuration
public class SecurityConfig {

    /**
     * Defines the standard PasswordEncoder (BCrypt) for hashing sensitive data (like PINs).
     * @return The BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt is the standard, secure, and industry-recommended hashing algorithm.
        return new BCryptPasswordEncoder();
    }
}