package com.stackclonell.stackclone.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service to register and update custom application metrics.
 * This provides LLD Observability for our patterns.
 */
@Service
public class CustomMetricService {

    private final Counter closedQuestionsCounter;
    private final Counter reputationUpdateCounter;
    private final AtomicInteger cacheHitGauge = new AtomicInteger(0);

    public CustomMetricService(MeterRegistry meterRegistry) {

        // 1. Counter: Tracks the total number of times an event occurred
        this.closedQuestionsCounter = Counter.builder("questions.closed.total")
                .description("Total number of questions closed via the State Pattern")
                .tags("status", "closed")
                .register(meterRegistry);

        this.reputationUpdateCounter = Counter.builder("reputation.updates.total")
                .description("Total reputation updates (Observer Pattern execution)")
                .tags("event", "voted")
                .register(meterRegistry);

        // 2. Gauge: Tracks the current value of a metric (e.g., Cache Hit Ratio, Queue Size)
        Gauge.builder("application.cache.hit.ratio", cacheHitGauge, AtomicInteger::get)
                .description("Simulated Cache Hit Ratio (0-100)")
                .tags("type", "redis")
                .register(meterRegistry);
    }


    // --- Public methods to be called by LLD patterns ---

    // Called when the PostState transitions to ClosedState
    public void incrementClosedQuestionCounter() {
        this.closedQuestionsCounter.increment();
    }

    // Called inside the ReputationService's @EventListener
    public void incrementReputationUpdateCounter() {
        this.reputationUpdateCounter.increment();
    }

    // Called periodically or after a cache lookup
    public void updateCacheHitRatio(int ratio) {
        this.cacheHitGauge.set(ratio);
    }
}