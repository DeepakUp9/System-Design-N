package com.jigsaw.game.api.listener;

import com.jigsaw.game.api.event.GamePiecePlacedEvent;
import com.jigsaw.game.api.event.GameSolvedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Concrete Observer/Kafka Consumer: Handles audit logging for all game events.
 * This ensures that if the audit database is slow, it doesn't block the main game thread.
 */
@Component
public class AuditListener {

    private static final String PIECE_PLACED_TOPIC = "jigsaw.game.piece.placed";
    private static final String GAME_SOLVED_TOPIC = "jigsaw.game.solved";
    private static final String GROUP_ID = "audit-group"; // All Observers in this group receive the message

    @KafkaListener(topics = PIECE_PLACED_TOPIC, groupId = GROUP_ID)
    public void handlePiecePlaced(GamePiecePlacedEvent event) {
        // Production Action: Save the event to a separate Audit/Analytics database (e.g., MongoDB, Snowflake)
        System.out.printf("[AUDIT_LOG] Handled Piece Placed Event: Game %s, Piece %s at (%d, %d).%n",
                event.gameId().toString().substring(0, 4),
                event.pieceId().toString().substring(0, 4),
                event.row(),
                event.col());
    }

    @KafkaListener(topics = GAME_SOLVED_TOPIC, groupId = GROUP_ID)
    public void handleGameSolved(GameSolvedEvent event) {
        // Production Action: Trigger final score calculation, send notifications, update leaderboards.
        System.out.printf("[AUDIT_LOG] Handled Game Solved Event: Game %s finalized in state %s.%n",
                event.gameId().toString().substring(0, 4),
                event.finalState());
    }
}