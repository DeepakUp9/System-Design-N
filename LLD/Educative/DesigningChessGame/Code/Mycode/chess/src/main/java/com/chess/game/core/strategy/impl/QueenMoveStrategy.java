package com.chess.game.core.strategy.impl;

import com.chess.game.core.model.Board;
import com.chess.game.core.model.Color;
import com.chess.game.core.model.Move;
import com.chess.game.core.model.Position;
import com.chess.game.core.strategy.MoveStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * CONCRETE STRATEGY: Implements the Queen's movement rules (Orthogonal + Diagonal).
 * Note: A better LLD approach for the Queen would be to use a Composite or Decorator
 * pattern to combine RookMoveStrategy and BishopMoveStrategy, but combining the logic
 * directly here is the most common and efficient implementation for a chess engine.
 */
public class QueenMoveStrategy implements MoveStrategy {

    // All 8 directions: Orthogonal + Diagonal
    private static final int[][] DIRECTIONS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Rook directions
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Bishop directions
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
                    break;
                }

                if (!board.isOccupied(nextPos)) {
                    legalMoves.add(new Move(currentPos, nextPos));
                } else {
                    if (board.getPiece(nextPos).getColor() != color) {
                        legalMoves.add(new Move(currentPos, nextPos)); // Capture
                    }
                    break; // Blocked
                }
            }
        }

        return legalMoves;
    }
}