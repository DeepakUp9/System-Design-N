package com.jigsaw.game.model;

import lombok.Value;

/**
 * Value Object representing a tile's position on the Jigsaw board.
 * Immutable and self-validating (if necessary, though coordinates are simple).
 */
@Value // Lombok's @Value creates immutable class with getters, constructor, equals, and hashCode.
public class BoardPosition {

    private final int row;
    private final int column;

    /**
     * Production validation: Ensures coordinates are non-negative.
     */
    public BoardPosition {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Board position coordinates must be non-negative.");
        }
    }
}