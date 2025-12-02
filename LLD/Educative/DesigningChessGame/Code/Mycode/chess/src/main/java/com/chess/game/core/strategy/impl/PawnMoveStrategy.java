package com.chess.game.core.strategy.impl;

import com.chess.game.core.model.Board;
import com.chess.game.core.model.Color;
import com.chess.game.core.model.Move;
import com.chess.game.core.model.Position;
import com.chess.game.core.strategy.MoveStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * CONCRETE STRATEGY: Implements the Pawn's complex movement rules.
 */
public class PawnMoveStrategy implements MoveStrategy {

    @Override
    public List<Move> calculateLegalMoves(Board board, Position currentPos, Color color) {
        List<Move> legalMoves = new ArrayList<>();

        // Direction is +1 for White (up) and -1 for Black (down) based on board index (0 to 7)
        int direction = (color == Color.WHITE) ? 1 : -1;
        int startRow = (color == Color.WHITE) ? 1 : 6; // White starts on row 1, Black on row 6

        // --- 1. Forward Single Move ---
        Position oneStep = currentPos.offset(direction, 0);
        if (oneStep.isValid() && !board.isOccupied(oneStep)) {
            legalMoves.add(new Move(currentPos, oneStep));

            // --- 2. Forward Double Move (Only from starting rank) ---
            if (currentPos.row() == startRow) {
                Position twoSteps = currentPos.offset(direction * 2, 0);
                if (twoSteps.isValid() && !board.isOccupied(twoSteps)) {
                    legalMoves.add(new Move(currentPos, twoSteps));
                }
            }
        }

        // --- 3. Diagonal Captures ---
        int[] captureCols = {-1, 1}; // Left and Right
        for (int dCol : captureCols) {
            Position capturePos = currentPos.offset(direction, dCol);

            if (capturePos.isValid() && board.isOccupied(capturePos)) {
                // Must capture an OPPONENT's piece
                if (board.getPiece(capturePos).getColor() != color) {
                    legalMoves.add(new Move(currentPos, capturePos));
                }
            }
        }

        // NOTE: En Passant and Promotion logic would be added here to achieve
        // full production-level compliance, requiring context like the last move made.

        return legalMoves;
    }
}
