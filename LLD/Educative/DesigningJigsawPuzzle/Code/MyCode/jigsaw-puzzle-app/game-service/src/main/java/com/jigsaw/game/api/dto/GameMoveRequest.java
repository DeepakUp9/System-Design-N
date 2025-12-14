package com.jigsaw.game.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

/**
 * Request DTO for placing a piece on the board.
 * Production-ready: Uses Jakarta Bean Validation to ensure input quality before hitting the service.
 */
public record GameMoveRequest(
        @NotNull(message = "Piece ID is mandatory")
        UUID pieceId,

        @PositiveOrZero(message = "Row must be non-negative")
        int row,

        @PositiveOrZero(message = "Column must be non-negative")
        int col,

        // Security Note: In a real system, the Player ID would be extracted from the JWT token, not the body.
        @NotNull(message = "Player ID is mandatory for authorization")
        UUID playerId
) {}