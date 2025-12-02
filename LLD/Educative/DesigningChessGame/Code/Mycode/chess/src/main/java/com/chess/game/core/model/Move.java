package com.chess.game.core.model;

/**
 * Represents a single move from a start position to an end position.
 */
public record Move(Position start, Position end) {
    // Future extensions will involve fields for special moves (castling, en passant)
    // and piece captured.

    @Override
    public String toString() {
        return "Move: " + start.row() + start.col() + " -> " + end.row() + end.col();
    }
}