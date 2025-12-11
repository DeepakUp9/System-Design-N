package com.rms.application.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Production-level Spring Security Configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Password Encoder (BCrypt is the industry standard)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Authentication Manager (Wires up the UserDetailsService and PasswordEncoder)
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    // 3. Security Filter Chain (The core authorization rules)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS and CSRF protection settings
                .csrf(AbstractHttpConfigurer::disable) // Typically disabled for stateless REST APIs
                .cors(AbstractHttpConfigurer::disable)

                // Authorization Rules
                .authorizeHttpRequests(authz -> authz
                        // Open endpoints (e.g., health checks)
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/**")).permitAll()

                        // High-level access for managing users and branches
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/admin/**")).hasRole("ADMIN")

                        // Manager-level access (e.g., pricing, reports)
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/manager/**")).hasAnyRole("ADMIN", "MANAGER")

                        // Waiter/Kitchen access (Order and Table management)
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/orders")).hasAnyRole("WAITER", "ONLINE_HANDLER")
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/v1/orders/**")).hasAnyRole("WAITER", "KITCHEN", "ONLINE_HANDLER")

                        // Allow read access for all authenticated users
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/**")).authenticated()


                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                // Resilience: Custom handlers for unauthorized and forbidden access
                .exceptionHandling(exceptions -> exceptions
                        // 401 Unauthorized: Triggered when authentication fails
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getOutputStream().println("{\"error\": \"Unauthorized\", \"message\": \"Authentication required or invalid credentials.\"}");
                        })
                        // 403 Forbidden: Triggered when user is authenticated but lacks required role
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getOutputStream().println("{\"error\": \"Forbidden\", \"message\": \"You do not have the required role to access this resource.\"}");
                        })
                )

                // Standard HTTP Basic Authentication for simplicity in initial testing
                // For production JWT/OAuth2 flow would be integrated here.
                .httpBasic(httpBasicConfigurer -> httpBasicConfigurer.realmName("RMS-API"))

                // Session Management: Stateless for REST API and better scalability
                .sessionManagement(sess -> sess.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS));

        return http.build();
    }
}