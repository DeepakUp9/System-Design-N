package com.jigsaw.game.api.publisher;

import com.jigsaw.game.api.event.GamePiecePlacedEvent;
import com.jigsaw.game.api.event.GameSolvedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * The Subject/Publisher component of the Observer Pattern (via Kafka).
 * Decouples the core service logic from where the events go (logs, web sockets, etc.).
 */
@Component
@RequiredArgsConstructor
public class GameEventPublisher {

    // Inject Spring's Kafka template
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Production Kafka Topic names
    private static final String PIECE_PLACED_TOPIC = "jigsaw.game.piece.placed";
    private static final String GAME_SOLVED_TOPIC = "jigsaw.game.solved";

    public void publishPiecePlacedEvent(GamePiecePlacedEvent event) {
        // Asynchronous publish. Keying by gameId ensures ordered processing per game.
        kafkaTemplate.send(PIECE_PLACED_TOPIC, event.gameId().toString(), event);
        System.out.printf("[PUBLISHER] Piece Placed Event published for Game %s.%n", event.gameId());
    }

    public void publishGameSolvedEvent(GameSolvedEvent event) {
        kafkaTemplate.send(GAME_SOLVED_TOPIC, event.gameId().toString(), event);
        System.out.printf("[PUBLISHER] Game Solved Event published for Game %s.%n", event.gameId());
    }
}