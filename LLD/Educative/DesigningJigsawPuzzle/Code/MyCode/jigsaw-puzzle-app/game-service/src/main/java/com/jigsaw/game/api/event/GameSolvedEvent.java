package com.jigsaw.game.api.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event DTO for notifying Observers that the game state has transitioned to SOLVED.
 */
public record GameSolvedEvent(
        UUID gameId,
        UUID winningPlayerId, // Could be null for multiplayer
        String finalState,
        LocalDateTime timestamp
) {}