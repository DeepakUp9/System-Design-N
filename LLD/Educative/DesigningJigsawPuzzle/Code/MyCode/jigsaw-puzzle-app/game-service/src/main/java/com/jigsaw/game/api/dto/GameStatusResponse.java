package com.jigsaw.game.api.dto;

import com.jigsaw.game.model.JigsawGame;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for providing the current game status.
 */
public record GameStatusResponse(
        UUID gameId,
        String gameState, // The LLD State name (e.g., InProgressState)
        boolean moveSuccessful,
        String message,
        LocalDateTime timestamp
) {
    /**
     * Factory method to map the LLD GameContext to a clean DTO.
     * This decouples the API contract from the internal domain model structure.
     */
    public static GameStatusResponse from(JigsawGame game, boolean success, String msg) {
        return new GameStatusResponse(
                game.getGameId(),
                game.getCurrentState().getClass().getSimpleName(),
                success,
                msg,
                LocalDateTime.now()
        );
    }
}