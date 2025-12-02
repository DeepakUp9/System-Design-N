package com.chess.game.core.strategy.impl;

import com.chess.game.core.model.Board;
import com.chess.game.core.model.Color;
import com.chess.game.core.model.Move;
import com.chess.game.core.model.Position;
import com.chess.game.core.strategy.MoveStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * CONCRETE STRATEGY: Implements the Bishop's movement rules (diagonal).
 */
public class BishopMoveStrategy implements MoveStrategy {

    // Diagonal directions: [Up-Left], [Up-Right], [Down-Left], [Down-Right]
    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    @Override
    public List<Move> calculateLegalMoves(Board board, Position currentPos, Color color) {
        List<Move> legalMoves = new ArrayList<>();

        for (int[] direction : DIRECTIONS) {
            int dRow = direction[0];
            int dCol = direction[1];

            // Loop through the direction until edge or obstacle is hit
            for (int i = 1; i < 8; i++) {
                Position nextPos = currentPos.offset(dRow * i, dCol * i);

                if (!nextPos.isValid()) {
                    break; // Off the board
                }

                if (!board.isOccupied(nextPos)) {
                    // Empty square: Add move and continue
                    legalMoves.add(new Move(currentPos, nextPos));
                } else {
                    // Occupied square: Check for capture
                    if (board.getPiece(nextPos).getColor() != color) {
                        legalMoves.add(new Move(currentPos, nextPos)); // Capture
                    }
                    // Stop searching in this direction
                    break;
                }
            }
        }

        return legalMoves;
    }
}