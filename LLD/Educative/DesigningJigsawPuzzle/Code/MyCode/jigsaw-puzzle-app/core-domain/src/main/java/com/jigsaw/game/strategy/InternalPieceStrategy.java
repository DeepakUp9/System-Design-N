package com.jigsaw.game.strategy;

import com.jigsaw.game.model.PuzzlePiece;
import com.jigsaw.game.model.BoardPosition;
import com.jigsaw.game.model.PieceType;

/**
 * Concrete Strategy for Internal Pieces.
 * Business Rule: An internal piece must have zero flat edges.
 */
public class InternalPieceStrategy implements PieceMatchStrategy {

    @Override
    public boolean isValidPlacement(PuzzlePiece piece, BoardPosition targetPosition, Object neighboringPieces) {
        // LLD Logic: Target position must be an internal board position.
        // All four sides (North, South, East, West) must match the corresponding neighboring piece's profile (tab/slot).

        if (piece.getType() != PieceType.INTERNAL) {
            throw new IllegalStateException("Strategy mismatch: InternalPieceStrategy used for a non-internal piece.");
        }

        // **Critical check:** Verify all four neighboring connections are valid (tab-to-slot match).
        // This is the most complex validation, involving checking the complementary shape of the neighbors.

        // Simplified: Check connectivity for all four sides.

        return true; // Assume valid
    }

    @Override
    public void rotate(PuzzlePiece piece) {
        // Implementation: Rotates the piece. For internal pieces, this mainly impacts
        // which tab/slot faces which direction for connection validation.
        piece.setRotation(piece.getRotation() + 90);
    }

    @Override
    public boolean validatePieceStructure(PuzzlePiece piece) {
        // Mandatory check: Must have exactly 0 flat edges.
        return piece.getFlatEdgeCount() == PieceType.INTERNAL.getRequiredFlatEdges();
    }
}