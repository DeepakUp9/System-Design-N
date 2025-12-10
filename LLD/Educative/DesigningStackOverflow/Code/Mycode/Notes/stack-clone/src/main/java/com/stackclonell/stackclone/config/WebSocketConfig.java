package com.stackclonell.stackclone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures the Spring WebSocket Message Broker using STOMP.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers the STOMP endpoint that clients will connect to.
     * We use SockJS for fallback options in browsers that don't natively support WebSockets.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Clients connect to: ws://localhost:8080/ws-connect
        registry.addEndpoint("/ws-connect")
                .setAllowedOriginPatterns("*") // Production: Restrict origins to your frontend domain
                .withSockJS();
    }

    /**
     * Configures the message broker for handling messages.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 1. Destination prefixes for messages from the server to the client (subscriptions)
        // Used for pushing updates: /topic/questions/{id}, /user/queue/reputation
        registry.enableSimpleBroker("/topic", "/user");

        // 2. Destination prefixes for messages from the client to the server (e.g., sending a chat message)
        registry.setApplicationDestinationPrefixes("/app");

        // Note: For horizontal scalability across multiple instances,
        // enableSimpleBroker should be replaced with an external broker like RabbitMQ or ActiveMQ.

        // --- Enterprise Scale: Use an external broker for inter-instance messaging ---
        // This requires an external system like RabbitMQ, ActiveMQ, or Redis Pub/Sub.
        // registry.enableStompBrokerRelay("/topic", "/queue")
        //     .setRelayHost("rabbitmq")
        //     // .setClientLogin("guest")
        //     // .setClientPasscode("guest");

        // For now, we revert to simple broker but acknowledge the limitation:
        registry.enableSimpleBroker("/topic", "/user");
    }
}