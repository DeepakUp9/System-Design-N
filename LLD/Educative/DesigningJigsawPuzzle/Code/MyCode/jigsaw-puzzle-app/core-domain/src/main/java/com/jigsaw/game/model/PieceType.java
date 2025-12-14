package com.jigsaw.game.model;

import lombok.Getter;

/**
 * Defines the immutable types of a puzzle piece, which directly correlates
 * to the Concrete Strategy implementation used.
 * Production note: The required 'flat edges' count is a strict structural constraint.
 */
@Getter
public enum PieceType {

    // Requires exactly 2 flat edges (two corners on a square board).
    CORNER(2),

    // Requires exactly 1 flat edge.
    EDGE(1),

    // Requires 0 flat edges.
    INTERNAL(0);

    private final int requiredFlatEdges;

    PieceType(int requiredFlatEdges) {
        this.requiredFlatEdges = requiredFlatEdges;
    }
}