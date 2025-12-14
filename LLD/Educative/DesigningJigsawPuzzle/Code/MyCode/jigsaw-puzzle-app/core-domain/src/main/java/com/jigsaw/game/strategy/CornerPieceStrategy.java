package com.jigsaw.game.strategy;

import com.jigsaw.game.model.PuzzlePiece;
import com.jigsaw.game.model.BoardPosition;
import com.jigsaw.game.model.PieceType;

/**
 * Concrete Strategy for Corner Pieces.
 * Business Rule: A corner piece must have exactly two flat edges.
 */
public class CornerPieceStrategy implements PieceMatchStrategy {

    @Override
    public boolean isValidPlacement(PuzzlePiece piece, BoardPosition targetPosition, Object neighboringPieces) {
        // LLD Logic: A production system would check that the targetPosition is a valid corner
        // (e.g., (0,0), (0,N), (N,0), (N,N)) AND that the two flat edges align with the board edges.

        // Placeholder for complex validation
        if (piece.getType() != PieceType.CORNER) {
            throw new IllegalStateException("Strategy mismatch: CornerPieceStrategy used for a non-corner piece.");
        }

        // **Critical check:** Ensure the piece's flat sides are against the board's edge.
        // Simplified: Check connectivity only for the two interior sides.

        // In a real system, we iterate over the piece's four sides and match non-flat sides
        // to neighboring tabs/slots, ensuring the two flat sides face the outside.

        return true; // Assume valid for now; full logic requires detailed Piece/Board models.
    }

    @Override
    public void rotate(PuzzlePiece piece) {
        // Implementation: Updates the piece's current rotation state (0, 90, 180, 270 degrees)
        // and recalculates which of its two flat sides are currently aligned with the board edges.
        piece.setRotation(piece.getRotation() + 90);
    }

    @Override
    public boolean validatePieceStructure(PuzzlePiece piece) {
        // Mandatory check: Must have exactly 2 flat edges.
        return piece.getFlatEdgeCount() == PieceType.CORNER.getRequiredFlatEdges();
    }
}