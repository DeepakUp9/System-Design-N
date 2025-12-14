package com.jigsaw.game.strategy;

import com.jigsaw.game.model.PuzzlePiece;
import com.jigsaw.game.model.BoardPosition;

/**
 * The Strategy Interface for determining if a piece is validly placed
 * and how it behaves (e.g., rotation/matching).
 * This ensures the core JigsawGame context is decoupled from specific piece types.
 */
public interface PieceMatchStrategy {

    /**
     * Production-ready method to check if the proposed piece can be placed at the
     * target board position based on its connections and orientation.
     *
     * @param piece The piece being placed.
     * @param targetPosition The position on the board where the piece is intended to be placed.
     * @param neighboringPieces A set of neighboring pieces (e.g., Map<Direction, PuzzlePiece>) to check against.
     * @return true if the piece successfully matches all its neighbors at the target position.
     * @throws IllegalStateException if the piece is attempted to be placed in an invalid state.
     */
    boolean isValidPlacement(PuzzlePiece piece, BoardPosition targetPosition, Object neighboringPieces); // Use Object for simplicity now, refined DTO later

    /**
     * Rotates the piece by 90 degrees clockwise and updates its internal state.
     * @param piece The piece to rotate.
     */
    void rotate(PuzzlePiece piece);

    /**
     * Checks if the piece's initial properties (like number of flat edges) match its type.
     * This is a critical validation step during game setup.
     * @param piece The piece to validate.
     * @return true if the piece is correctly classified and formed.
     */
    boolean validatePieceStructure(PuzzlePiece piece);
}