package com.amazon.orderservice.infrastructure.messaging;

import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.infrastructure.messaging.events.OrderPlacedEvent;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

// This simulates publishing a message to a Kafka/RabbitMQ topic
@Component
public class OrderEventPublisher {

    // In a real app, this would be a KafkaTemplate or JmsTemplate
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Event type constants
    public static final String ORDER_PLACED_TOPIC = "order.events.placed";
    public static final String ORDER_CANCELLED_TOPIC = "order.events.cancelled";

    /**
     * Publishes an event to the message broker.
     */
    public void publishOrderPlacedEvent(Order order) {
        try {
            // Build the event message payload (Production standard: include only necessary fields)
            String payload = objectMapper.writeValueAsString(new OrderPlacedEvent(order));

            // --- Simulated Message Broker Send ---
            System.out.println("--- OBSERVER PATTERN: PUBLISH ---");
            System.out.println("ORDER SERVICE: Publishing event to topic [" + ORDER_PLACED_TOPIC + "]");
            System.out.println("PAYLOAD: " + payload);
            System.out.println("---------------------------------");

            // In a real system, the Message Broker client sends the message here.

        } catch (Exception e) {
            // Resilience: Handle serialization or messaging errors (e.g., dead letter queue)
            System.err.println("Failed to publish order placed event: " + e.getMessage());
        }
    }

    // Similarly, a publishOrderCancelledEvent method would be implemented.
}