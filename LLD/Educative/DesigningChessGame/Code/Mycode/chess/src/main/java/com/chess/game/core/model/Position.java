package com.chess.game.core.model;

/**
 * Represents a specific square on the board (e.g., A1, E4).
 * Using a record for immutable data transfer object (DTO) clarity.
 */
public record Position(int row, int col) {
    // Standard 8x8 board: row (0-7), col (0-7)

    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    // Optional: convenience methods for movement checks
    public Position offset(int dRow, int dCol) {
        return new Position(this.row + dRow, this.col + dCol);
    }
}