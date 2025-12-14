package com.stockbrokerage.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security Configuration: Sets up authentication, authorization, and password hashing.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    // --- Core Security Beans ---

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Production standard: BCrypt for secure password hashing
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // --- Authentication Provider (DAO) ---

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // --- Authorization Filter Chain ---

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Production Rule: Disable CSRF for stateless REST APIs
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Stateless Session Policy (essential for JWT/REST)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Authorization Rules (Lock down the API)
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to register and H2 Console (in dev)
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // 4. Authentication Mechanism: Use HTTP Basic Auth for simplicity in this LLD,
                //    but production would use JWT or OAuth2.
                .httpBasic(httpBasic -> httpBasic.realmName("StockBrokerageSystem"))

                // 5. Ensure authentication provider is used
                .authenticationProvider(authenticationProvider());

        // Required for H2 console to work when CSRF is disabled
        http.headers(headers -> headers.frameOptions().sameOrigin());

        return http.build();
    }
}