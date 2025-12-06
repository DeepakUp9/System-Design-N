package com.hms.hotel.config;

import com.hms.hotel.security.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enables method-level security checks (@PreAuthorize)
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for stateless REST APIs
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints (Authentication Controller)
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // RBAC Policy (Authorization)
                        .requestMatchers(HttpMethod.POST, "/api/v1/rooms/**").hasAnyAuthority(Role.MANAGER.name(), Role.FRONT_DESK.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/bookings/**").hasAnyAuthority(Role.FRONT_DESK.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/rooms/**").hasAnyAuthority(Role.MANAGER.name(), Role.FRONT_DESK.name())

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session for JWT
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}