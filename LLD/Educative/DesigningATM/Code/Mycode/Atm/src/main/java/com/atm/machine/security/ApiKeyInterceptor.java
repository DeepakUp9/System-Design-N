package com.atm.machine.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * Interceptor to enforce API Key validation on all requests.
 * Ensures the client (ATM terminal) is authorized to communicate with the service.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyInterceptor implements HandlerInterceptor {

    // Inject the expected API key from application.properties
    @Value("${atm.security.api-key}")
    private  String expectedApiKey;

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String ERROR_MESSAGE = "Missing or Invalid API Key.";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String providedApiKey = request.getHeader(API_KEY_HEADER);

        if (providedApiKey == null || !providedApiKey.equals(expectedApiKey)) {
            log.warn("Unauthorized API access attempt from {}. Provided key: {}",
                    request.getRemoteAddr(), providedApiKey != null ? providedApiKey : "N/A");

            // Set error status and message
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"" + ERROR_MESSAGE + "\"}");

            return false; // Stop further processing
        }

        return true; // Key is valid, proceed to the next interceptor or controller
    }
}