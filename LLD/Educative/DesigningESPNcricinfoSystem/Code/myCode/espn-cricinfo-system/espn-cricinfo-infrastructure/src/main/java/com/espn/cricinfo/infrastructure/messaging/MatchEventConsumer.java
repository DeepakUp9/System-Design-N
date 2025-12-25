package com.espn.cricinfo.infrastructure.messaging;

import com.espn.cricinfo.infrastructure.cache.CacheService;
import com.espn.cricinfo.infrastructure.messaging.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer service for processing match-related events.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MatchEventConsumer {

    private final CacheService cacheService;

    /**
     * Consume match events.
     */
    @KafkaListener(topics = "match-events", groupId = "match-event-processor")
    public void consumeMatchEvents(@Payload BaseEvent event,
                                 @Header(KafkaHeaders.RECEIVED_KEY) String key,
                                 Acknowledgment acknowledgment) {
        try {
            log.debug("Consumed match event: {} for key: {}", event.getEventType(), key);

            switch (event.getEventType()) {
                case "MatchCreated" -> handleMatchCreated((MatchCreatedEvent) event);
                case "MatchStarted" -> handleMatchStarted((MatchStartedEvent) event);
                case "MatchCompleted" -> handleMatchCompleted((MatchCompletedEvent) event);
                default -> log.warn("Unknown event type: {}", event.getEventType());
            }

            acknowledgment.acknowledge();

        } catch (Exception e) {
            log.error("Error processing match event: {}", event.getEventId(), e);
            // In production, you might want to send to dead letter queue
            acknowledgment.acknowledge(); // or nacknowledge() for retry
        }
    }

    /**
     * Consume ball scoring events.
     */
    @KafkaListener(topics = "ball-events", groupId = "ball-event-processor")
    public void consumeBallEvents(@Payload BallScoredEvent event,
                                @Header(KafkaHeaders.RECEIVED_KEY) String key,
                                Acknowledgment acknowledgment) {
        try {
            log.debug("Consumed ball event: {} for match: {}", event.getEventType(), key);

            handleBallScored(event);

            acknowledgment.acknowledge();

        } catch (Exception e) {
            log.error("Error processing ball event: {}", event.getEventId(), e);
            acknowledgment.acknowledge();
        }
    }

    /**
     * Consume live scoring events.
     */
    @KafkaListener(topics = "live-scoring", groupId = "live-scoring-processor")
    public void consumeLiveScoringEvents(@Payload BaseEvent event,
                                       @Header(KafkaHeaders.RECEIVED_KEY) String key,
                                       Acknowledgment acknowledgment) {
        try {
            log.debug("Consumed live scoring event: {} for match: {}", event.getEventType(), key);

            // Update live scoring cache
            String cacheKey = "live:match:" + key;
            cacheService.set(cacheKey, event);

            // Set cache expiration for live events (e.g., 1 hour)
            cacheService.expire(cacheKey, 1, java.util.concurrent.TimeUnit.HOURS);

            acknowledgment.acknowledge();

        } catch (Exception e) {
            log.error("Error processing live scoring event: {}", event.getEventId(), e);
            acknowledgment.acknowledge();
        }
    }

    /**
     * Handle match created event.
     */
    private void handleMatchCreated(MatchCreatedEvent event) {
        log.info("Processing MatchCreatedEvent for match: {}", event.getAggregateId());

        // Cache match metadata
        String cacheKey = "match:metadata:" + event.getAggregateId();
        cacheService.set(cacheKey, event);

        // Initialize match statistics cache
        String statsKey = "match:stats:" + event.getAggregateId();
        cacheService.set(statsKey, new MatchStats());

        log.info("Match metadata cached for: {}", event.getAggregateId());
    }

    /**
     * Handle match started event.
     */
    private void handleMatchStarted(MatchStartedEvent event) {
        log.info("Processing MatchStartedEvent for match: {}", event.getAggregateId());

        // Update match status in cache
        String statusKey = "match:status:" + event.getAggregateId();
        cacheService.set(statusKey, "IN_PROGRESS");

        // Cache innings information
        String inningsKey = "match:innings:" + event.getAggregateId();
        cacheService.set(inningsKey, event.getInningsId());

        log.info("Match started status updated for: {}", event.getAggregateId());
    }

    /**
     * Handle ball scored event.
     */
    private void handleBallScored(BallScoredEvent event) {
        log.debug("Processing BallScoredEvent for match: {}", event.getAggregateId());

        // Update match statistics in cache
        String statsKey = "match:stats:" + event.getAggregateId();
        MatchStats stats = cacheService.get(statsKey, MatchStats.class);
        if (stats == null) {
            stats = new MatchStats();
        }

        // Update statistics based on ball
        stats.addRuns(event.getRunsScored());
        if (event.getIsWicket()) {
            stats.addWicket();
        }
        if (event.getIsBoundary()) {
            stats.addBoundary();
        }
        if (event.getIsSix()) {
            stats.addSix();
        }

        cacheService.set(statsKey, stats);

        // Update live score cache
        String scoreKey = "live:score:" + event.getAggregateId();
        cacheService.set(scoreKey, stats);

        log.debug("Ball statistics updated for match: {}", event.getAggregateId());
    }

    /**
     * Handle match completed event.
     */
    private void handleMatchCompleted(MatchCompletedEvent event) {
        log.info("Processing MatchCompletedEvent for match: {}", event.getAggregateId());

        // Update final match status
        String statusKey = "match:status:" + event.getAggregateId();
        cacheService.set(statusKey, "COMPLETED");

        // Cache final result
        String resultKey = "match:result:" + event.getAggregateId();
        cacheService.set(resultKey, event);

        // Remove live scoring cache
        String liveKey = "live:match:" + event.getAggregateId();
        cacheService.delete(liveKey);

        log.info("Match completion processed for: {}", event.getAggregateId());
    }

    /**
     * DTO for match statistics.
     */
    public static class MatchStats {
        private int totalRuns = 0;
        private int totalWickets = 0;
        private int totalBoundaries = 0;
        private int totalSixes = 0;
        private double runRate = 0.0;

        public void addRuns(int runs) {
            this.totalRuns += runs;
        }

        public void addWicket() {
            this.totalWickets += 1;
        }

        public void addBoundary() {
            this.totalBoundaries += 1;
        }

        public void addSix() {
            this.totalSixes += 1;
        }

        // Getters
        public int getTotalRuns() { return totalRuns; }
        public int getTotalWickets() { return totalWickets; }
        public int getTotalBoundaries() { return totalBoundaries; }
        public int getTotalSixes() { return totalSixes; }
        public double getRunRate() { return runRate; }
    }
}
