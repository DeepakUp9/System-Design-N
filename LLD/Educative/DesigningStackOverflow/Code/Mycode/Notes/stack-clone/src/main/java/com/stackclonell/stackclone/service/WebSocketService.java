package com.stackclonell.stackclone.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Utility service to push messages to clients via the STOMP message broker.
 */
@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Pushes an update about a new answer to all subscribers of a specific question.
     * @param questionId The ID of the question page to update.
     * @param payload The data to send (e.g., summary of the new answer).
     */
    public void notifyNewAnswer(Long questionId, Map<String, Object> payload) {
        String destination = "/topic/questions/" + questionId;
        messagingTemplate.convertAndSend(destination, payload);
        System.out.println("WebSocket: Notified subscribers of new answer on QID " + questionId);
    }

    /**
     * Pushes a private notification (e.g., reputation change) to a specific user.
     * @param username The target user's username.
     * @param payload The data to send.
     */
    public void notifyUserReputationChange(String username, Map<String, Object> payload) {
        // Sends to /user/{username}/queue/reputation
        messagingTemplate.convertAndSendToUser(username, "/queue/reputation", payload);
        System.out.println("WebSocket: Notified user " + username + " of reputation change.");
    }
}