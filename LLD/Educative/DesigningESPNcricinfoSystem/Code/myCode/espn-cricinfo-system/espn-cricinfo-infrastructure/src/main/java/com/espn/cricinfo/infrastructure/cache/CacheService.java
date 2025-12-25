package com.espn.cricinfo.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis cache service for managing cached data operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Set cache value with default TTL.
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            log.debug("Cached value for key: {}", key);
        } catch (Exception e) {
            log.error("Failed to cache value for key: {}", key, e);
        }
    }

    /**
     * Set cache value with custom TTL.
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            log.debug("Cached value for key: {} with TTL: {} {}", key, timeout, unit);
        } catch (Exception e) {
            log.error("Failed to cache value for key: {}", key, e);
        }
    }

    /**
     * Set cache value with Duration.
     */
    public void set(String key, Object value, Duration timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout);
            log.debug("Cached value for key: {} with TTL: {}", key, timeout);
        } catch (Exception e) {
            log.error("Failed to cache value for key: {}", key, e);
        }
    }

    /**
     * Get cached value.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                log.debug("Retrieved cached value for key: {}", key);
                return (T) value;
            }
        } catch (Exception e) {
            log.error("Failed to retrieve cached value for key: {}", key, e);
        }
        return null;
    }

    /**
     * Check if key exists in cache.
     */
    public boolean hasKey(String key) {
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(exists);
        } catch (Exception e) {
            log.error("Failed to check key existence: {}", key, e);
            return false;
        }
    }

    /**
     * Delete cache key.
     */
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
            log.debug("Deleted cache key: {}", key);
        } catch (Exception e) {
            log.error("Failed to delete cache key: {}", key, e);
        }
    }

    /**
     * Delete multiple keys.
     */
    public void delete(String... keys) {
        try {
            redisTemplate.delete(Set.of(keys));
            log.debug("Deleted cache keys: {}", (Object) keys);
        } catch (Exception e) {
            log.error("Failed to delete cache keys", e);
        }
    }

    /**
     * Set expiration time for key.
     */
    public void expire(String key, long timeout, TimeUnit unit) {
        try {
            redisTemplate.expire(key, timeout, unit);
            log.debug("Set expiration for key: {} to {} {}", key, timeout, unit);
        } catch (Exception e) {
            log.error("Failed to set expiration for key: {}", key, e);
        }
    }

    /**
     * Get time to live for key.
     */
    public Long getExpire(String key, TimeUnit unit) {
        try {
            return redisTemplate.getExpire(key, unit);
        } catch (Exception e) {
            log.error("Failed to get expiration for key: {}", key, e);
            return -1L;
        }
    }

    /**
     * Increment numeric value.
     */
    public Long increment(String key) {
        try {
            Long value = redisTemplate.opsForValue().increment(key);
            log.debug("Incremented key: {} to {}", key, value);
            return value;
        } catch (Exception e) {
            log.error("Failed to increment key: {}", key, e);
            return null;
        }
    }

    /**
     * Add to set.
     */
    public void addToSet(String key, Object... values) {
        try {
            redisTemplate.opsForSet().add(key, values);
            log.debug("Added values to set: {}", key);
        } catch (Exception e) {
            log.error("Failed to add to set: {}", key, e);
        }
    }

    /**
     * Get set members.
     */
    public Set<Object> getSetMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Failed to get set members: {}", key, e);
            return Set.of();
        }
    }

    /**
     * Add to sorted set with score.
     */
    public void addToSortedSet(String key, Object value, double score) {
        try {
            redisTemplate.opsForZSet().add(key, value, score);
            log.debug("Added to sorted set: {} with score: {}", key, score);
        } catch (Exception e) {
            log.error("Failed to add to sorted set: {}", key, e);
        }
    }

    /**
     * Get range from sorted set.
     */
    public Set<Object> getSortedSetRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error("Failed to get sorted set range: {}", key, e);
            return Set.of();
        }
    }

    /**
     * Clear all cache.
     */
    public void clearAll() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushAll();
            log.info("Cleared all cache");
        } catch (Exception e) {
            log.error("Failed to clear all cache", e);
        }
    }

    /**
     * Get cache statistics.
     */
    public CacheStats getCacheStats() {
        // This would integrate with Redis INFO command
        return new CacheStats(0, 0, 0, 0);
    }

    /**
     * Cache statistics DTO.
     */
    public record CacheStats(
            long totalKeys,
            long usedMemory,
            long hits,
            long misses
    ) {}
}
