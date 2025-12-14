package com.jigsaw.game.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Entity representing a player in the game.
 * In a production system, this would be linked to the separate User Service (via UUID).
 */
@Data
@Builder
public class Player {

    @Builder.Default
    private UUID playerId = UUID.randomUUID();
    private String username;
    private boolean isHost;

    // Player-specific state, e.g., points, status, inventory of pieces held.
    private int score;
}