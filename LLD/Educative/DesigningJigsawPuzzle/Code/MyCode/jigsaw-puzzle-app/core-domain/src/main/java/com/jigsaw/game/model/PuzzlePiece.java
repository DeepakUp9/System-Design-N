package com.jigsaw.game.model;

import com.jigsaw.game.strategy.PieceMatchStrategy;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

/**
 * The core entity representing a single piece.
 * CRITICAL: It acts as the Strategy Context, delegating behavior to its assigned strategy.
 * @Data creates getters/setters/equals/hashCode, @Builder simplifies construction.
 */
@Data
@Builder
public class PuzzlePiece {

    // Production-level identification
    @Builder.Default
    private UUID pieceId = UUID.randomUUID();

    @NonNull
    private final PieceType type; // The piece's type (Corner, Edge, Internal)

    // Piece State
    private int rotation; // Current rotation in degrees (0, 90, 180, 270)
    private BoardPosition currentPosition; // Null if not placed on the board

    // Strategy Context Reference (The key to LLD decoupling)
    private PieceMatchStrategy matchingStrategy;

    // Structural Property (Used by the Strategy's validatePieceStructure method)
    private final int flatEdgeCount;

    /**
     * Initializes the strategy based on the piece's type.
     * In a real Spring Boot application, this logic would be handled by a Factory
     * injected by Spring to manage the lifecycle of the stateless strategies.
     * (We will implement this factory in the `game-service` module later).
     */
    public void setMatchingStrategy(PieceMatchStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Matching Strategy cannot be null.");
        }
        this.matchingStrategy = strategy;
    }

    /**
     * Delegates the placement validation to the assigned Strategy.
     */
    public boolean place(BoardPosition targetPosition, Object neighboringPieces) {
        if (this.matchingStrategy == null) {
            throw new IllegalStateException("Piece strategy not initialized. Cannot place piece.");
        }
        // Core LLD: The Context delegates the heavy lifting to the Strategy.
        boolean isValid = this.matchingStrategy.isValidPlacement(this, targetPosition, neighboringPieces);

        if (isValid) {
            this.currentPosition = targetPosition; // Update state only if valid
        }
        return isValid;
    }

    /**
     * Delegates the rotation to the assigned Strategy.
     */
    public void rotatePiece() {
        if (this.matchingStrategy == null) {
            throw new IllegalStateException("Piece strategy not initialized. Cannot rotate piece.");
        }
        this.matchingStrategy.rotate(this);
    }
}