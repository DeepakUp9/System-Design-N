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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interceptor to enforce rate limiting based on the client's IP address.
 * Uses a simple in-memory token bucket/sliding window approach.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    // Key: Client IP Address (String)
    // Value: Timestamp of the last successful request (Long)
    private final Map<String, Long> requestTimestamps = new ConcurrentHashMap<>();

    // Key: Client IP Address (String)
    // Value: Current request count in the window (Integer)
    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();

    @Value("${atm.rate-limit.max-requests}")
    private int maxRequests;

    @Value("${atm.rate-limit.time-window-ms}")
    private long timeWindowMs;

    private static final String ERROR_MESSAGE = "Too many requests. Please try again later.";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String clientIp = request.getRemoteAddr();
        long now = System.currentTimeMillis();

        // 1. Sliding Window Logic
        Long lastRequestTime = requestTimestamps.getOrDefault(clientIp, 0L);
        Integer currentCount = requestCounts.getOrDefault(clientIp, 0);

        // If the time window has elapsed since the last request, reset the count.
        if (now - lastRequestTime > timeWindowMs) {
            requestCounts.put(clientIp, 1);
            requestTimestamps.put(clientIp, now);
        } else {
            // Check if limit is exceeded
            if (currentCount >= maxRequests) {
                log.warn("Rate limit exceeded for IP: {}", clientIp);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"status\": 429, \"error\": \"Too Many Requests\", \"message\": \"" + ERROR_MESSAGE + "\"}");
                return false;
            }
            // Increment count for the current window
            requestCounts.put(clientIp, currentCount + 1);
        }

        return true; // Rate limit check passed
    }
}