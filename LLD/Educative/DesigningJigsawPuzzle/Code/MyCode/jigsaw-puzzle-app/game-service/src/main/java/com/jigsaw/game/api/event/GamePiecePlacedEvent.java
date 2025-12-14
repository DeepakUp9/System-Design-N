
package com.jigsaw.game.api.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event DTO for notifying Observers that a piece was successfully placed.
 */
public record GamePiecePlacedEvent(
        UUID gameId,
        UUID pieceId,
        UUID playerId,
        int row,
        int col,
        LocalDateTime timestamp
) {
    public static GamePiecePlacedEvent create(UUID gameId, UUID pieceId, UUID playerId, int row, int col) {
        return new GamePiecePlacedEvent(gameId, pieceId, playerId, row, col, LocalDateTime.now());
    }
}