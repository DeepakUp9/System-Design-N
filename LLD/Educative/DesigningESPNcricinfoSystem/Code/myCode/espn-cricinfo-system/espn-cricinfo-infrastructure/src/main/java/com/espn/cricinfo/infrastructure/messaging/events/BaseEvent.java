package com.espn.cricinfo.infrastructure.messaging.events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base class for all domain events.
 * Provides common event properties like ID, timestamp, and event type.
 */
public abstract class BaseEvent {

    private final String eventId;
    private final String eventType;
    private final LocalDateTime timestamp;
    private final String aggregateId;
    private final String aggregateType;
    private final int version;

    protected BaseEvent(String eventType, String aggregateId, String aggregateType, int version) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.version = version;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return String.format("BaseEvent{eventId='%s', eventType='%s', aggregateId='%s', timestamp=%s}",
                eventId, eventType, aggregateId, timestamp);
    }
}
