package com.chess.game.core.strategy.impl;

import com.chess.game.core.model.Board;
import com.chess.game.core.model.Color;
import com.chess.game.core.model.Move;
import com.chess.game.core.model.Position;
import com.chess.game.core.strategy.MoveStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * CONCRETE STRATEGY: Implements the Knight's movement rules (L-shape).
 */
public class KnightMoveStrategy implements MoveStrategy {

    // All 8 possible "L" moves (2 units in one axis, 1 unit in the other)
    private static final int[][] OFFSETS = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
    };

    @Override
    public List<Move> calculateLegalMoves(Board board, Position currentPos, Color color) {
        List<Move> legalMoves = new ArrayList<>();

        for (int[] offset : OFFSETS) {
            Position nextPos = currentPos.offset(offset[0], offset[1]);

            if (nextPos.isValid()) {
                // Knight moves are never blocked, only checked for destination
                if (!board.isOccupied(nextPos) || board.getPiece(nextPos).getColor() != color) {
                    legalMoves.add(new Move(currentPos, nextPos));
                }
            }
        }

        return legalMoves;
    }
}