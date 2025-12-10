package com.stackclonell.stackclone.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Component: The central configuration for Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Password Encoder (Essential for security)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Strong, industry-standard hash
    }

    // 2. Authentication Manager (Used for login/token generation)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 3. Security Filter Chain (The access rules)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Scalability/Security: Disable CSRF for stateless REST API
                .csrf(csrf -> csrf.disable())

                // Security: Configure Session Management as stateless (for token-based auth)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Access Rules (Authorization)
                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to access the registration, login, and read endpoints
                        .requestMatchers("/api/auth/**",
                                "/api/questions/**",
                                "/api/users/**").permitAll()

                        // Allow actuators (for Resilience/Monitoring)
                        .requestMatchers("/actuator/**").permitAll()

                        // Require authentication for any other request (e.g., creating, editing, voting)
                        .anyRequest().authenticated()
                );

        // Production Note: In a real system, we would add JWT filter chain here:
        // http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}