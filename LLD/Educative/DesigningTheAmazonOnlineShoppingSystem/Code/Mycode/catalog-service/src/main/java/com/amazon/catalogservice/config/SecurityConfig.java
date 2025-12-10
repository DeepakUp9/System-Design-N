package com.amazon.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Enables Spring Security features
public class SecurityConfig {

    // 1. Authorization Rules (Access Control)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for Stateless REST APIs
                .authorizeHttpRequests(auth -> auth
                        // Public access for viewing products (Read Operations)
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()

                        // Admin/Moderator required for all Write/Delete operations
                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")

                        // Security for Actuator endpoints (Health Check)
                        .requestMatchers("/actuator/**").hasRole("MONITOR")

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                .httpBasic(); // Use HTTP Basic Authentication for now (to be replaced by JWT in production)

        return http.build();
    }

    // 2. User Authentication (In-Memory for simplicity)
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder() // Placeholder for a real password encoder
                .username("admin")
                .password("password123")
                .roles("ADMIN", "USER")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("userpass")
                .roles("USER")
                .build();

        UserDetails monitor = User.withDefaultPasswordEncoder()
                .username("monitor")
                .password("monitorpass")
                .roles("MONITOR")
                .build();

        return new InMemoryUserDetailsManager(admin, user, monitor);
    }

    // NOTE: In a true production environment, we would define a PasswordEncoder Bean
    // and replace InMemoryUserDetailsManager with a database-backed UserDetailsService.
}