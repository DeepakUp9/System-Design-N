package com.stackclonell.stackclone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Enables Spring Session to manage HTTP sessions and store them centrally in Redis.
 * This is CRITICAL for horizontal scaling and high availability, as it decouples the
 * session state from the application instance.
 */
@Configuration
// Max inactive interval in seconds (e.g., 30 minutes)
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class SessionConfig {
    // Configuration for Redis connection is already handled by application.yml
    //will do it later in this managed how does cacheIn and cache put working
}