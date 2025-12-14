package com.jigsaw.game.strategy;

import com.jigsaw.game.model.PuzzlePiece;
import com.jigsaw.game.model.BoardPosition;
import com.jigsaw.game.model.PieceType;

/**
 * Concrete Strategy for Edge Pieces.
 * Business Rule: An edge piece must have exactly one flat edge.
 */
public class EdgePieceStrategy implements PieceMatchStrategy {

    @Override
    public boolean isValidPlacement(PuzzlePiece piece, BoardPosition targetPosition, Object neighboringPieces) {
        // LLD Logic: Check that the targetPosition is a non-corner edge position
        // AND that the single flat edge is facing the outside of the board.

        if (piece.getType() != PieceType.EDGE) {
            throw new IllegalStateException("Strategy mismatch: EdgePieceStrategy used for a non-edge piece.");
        }

        // **Critical check:** Ensure the piece's flat side is against the board's edge.
        // Simplified: Check connectivity for the three interior sides.

        // In a real system, we verify that the piece's single flat side is correctly oriented
        // to the single board edge (North, South, East, or West) corresponding to its position.

        return true; // Assume valid
    }

    @Override
    public void rotate(PuzzlePiece piece) {
        // Implementation: Rotates and updates the orientation of the single flat edge.
        piece.setRotation(piece.getRotation() + 90);
    }

    @Override
    public boolean validatePieceStructure(PuzzlePiece piece) {
        // Mandatory check: Must have exactly 1 flat edge.
        return piece.getFlatEdgeCount() == PieceType.EDGE.getRequiredFlatEdges();
    }
}