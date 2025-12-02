package com.chess.game.core.strategy.impl;

import com.chess.game.core.model.Board;
import com.chess.game.core.model.Color;
import com.chess.game.core.model.Move;
import com.chess.game.core.model.Position;
import com.chess.game.core.strategy.MoveStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * CONCRETE STRATEGY: Implements the King's movement rules.
 */
public class KingMoveStrategy implements MoveStrategy {

    // Possible offsets for one step in any direction
    private static final int[] ROW_OFFSETS = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] COL_OFFSETS = {-1, 0, 1, -1, 1, -1, 0, 1};

    @Override
    public List<Move> calculateLegalMoves(Board board, Position currentPos, Color color) {
        List<Move> legalMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            Position nextPos = currentPos.offset(ROW_OFFSETS[i], COL_OFFSETS[i]);

            if (nextPos.isValid()) {
                // Check if the destination square is empty OR contains an opponent's piece
                if (!board.isOccupied(nextPos) || board.getPiece(nextPos).getColor() != color) {
                    legalMoves.add(new Move(currentPos, nextPos));
                }
            }
        }

        // NOTE: Castling logic would be added here, which requires checking if the King
        // and Rook haven't moved and if the squares are unattacked (complex game rule).

        return legalMoves;
    }
}