package com.espn.cricinfo.infrastructure.messaging;

import com.espn.cricinfo.infrastructure.messaging.events.BaseEvent;
import com.espn.cricinfo.infrastructure.messaging.events.BallScoredEvent;
import com.espn.cricinfo.infrastructure.messaging.events.MatchCompletedEvent;
import com.espn.cricinfo.infrastructure.messaging.events.MatchCreatedEvent;
import com.espn.cricinfo.infrastructure.messaging.events.MatchStartedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka producer service for publishing match-related events.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MatchEventProducer {

    private final KafkaTemplate<String, BaseEvent> kafkaTemplate;

    private static final String MATCH_EVENTS_TOPIC = "match-events";
    private static final String BALL_EVENTS_TOPIC = "ball-events";
    private static final String LIVE_SCORING_TOPIC = "live-scoring";

    /**
     * Publish match created event.
     */
    public void publishMatchCreated(MatchCreatedEvent event) {
        publishEvent(MATCH_EVENTS_TOPIC, event.getAggregateId(), event);
        log.info("Published MatchCreatedEvent for match: {}", event.getAggregateId());
    }

    /**
     * Publish match started event.
     */
    public void publishMatchStarted(MatchStartedEvent event) {
        publishEvent(MATCH_EVENTS_TOPIC, event.getAggregateId(), event);
        publishEvent(LIVE_SCORING_TOPIC, event.getAggregateId(), event);
        log.info("Published MatchStartedEvent for match: {}", event.getAggregateId());
    }

    /**
     * Publish ball scored event.
     */
    public void publishBallScored(BallScoredEvent event) {
        publishEvent(BALL_EVENTS_TOPIC, event.getAggregateId(), event);
        publishEvent(LIVE_SCORING_TOPIC, event.getAggregateId(), event);
        log.debug("Published BallScoredEvent for match: {} - {} runs",
                 event.getAggregateId(), event.getRunsScored());
    }

    /**
     * Publish match completed event.
     */
    public void publishMatchCompleted(MatchCompletedEvent event) {
        publishEvent(MATCH_EVENTS_TOPIC, event.getAggregateId(), event);
        publishEvent(LIVE_SCORING_TOPIC, event.getAggregateId(), event);
        log.info("Published MatchCompletedEvent for match: {}", event.getAggregateId());
    }

    /**
     * Generic method to publish events to Kafka.
     */
    private void publishEvent(String topic, String key, BaseEvent event) {
        try {
            CompletableFuture<SendResult<String, BaseEvent>> future =
                    kafkaTemplate.send(topic, key, event);

            future.whenComplete((result, exception) -> {
                if (exception != null) {
                    log.error("Failed to send event to topic {}: {}", topic, exception.getMessage());
                } else {
                    log.debug("Event sent successfully to topic {} with offset {}",
                             topic, result.getRecordMetadata().offset());
                }
            });

        } catch (Exception e) {
            log.error("Error publishing event to topic {}: {}", topic, e.getMessage());
            // Consider implementing retry logic or dead letter queue here
        }
    }

    /**
     * Publish event with custom headers.
     */
    public void publishEventWithHeaders(String topic, String key, BaseEvent event,
                                      java.util.Map<String, String> headers) {
        try {
            org.springframework.kafka.support.KafkaHeaders kafkaHeaders =
                    new org.springframework.kafka.support.KafkaHeaders();

            org.springframework.messaging.Message<BaseEvent> message =
                    org.springframework.messaging.support.MessageBuilder
                            .withPayload(event)
                            .setHeader(org.springframework.kafka.support.KafkaHeaders.TOPIC, topic)
                            .setHeader(org.springframework.kafka.support.KafkaHeaders.KEY, key)
                            .copyHeaders(headers)
                            .build();

            kafkaTemplate.send(message);
            log.debug("Published event with headers to topic: {}", topic);

        } catch (Exception e) {
            log.error("Error publishing event with headers to topic {}: {}", topic, e.getMessage());
        }
    }

    /**
     * Send bulk events (for high-throughput scenarios).
     */
    public void publishBulkEvents(java.util.List<BaseEvent> events, String topic) {
        events.forEach(event -> publishEvent(topic, event.getAggregateId(), event));
        log.info("Published {} events to topic: {}", events.size(), topic);
    }
}
