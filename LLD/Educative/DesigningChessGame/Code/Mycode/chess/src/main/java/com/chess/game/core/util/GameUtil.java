package com.chess.game.core.util;

import com.chess.game.core.model.Color;
import com.chess.game.core.model.Move;
import com.chess.game.core.model.Piece;
import com.chess.game.core.model.Position;
import com.chess.game.core.model.Board;


import java.util.List;
import java.util.Optional;
/**
 * UTILITY CLASS: Handles complex, non-state-specific board logic (e.g., Is King in Check?).
 * This keeps the State and Strategy objects clean and focused.
 */
public class GameUtil {

    /**
     * Finds the King of a specific color on the board.
     */
    public static Optional<Position> findKingPosition(Board board, Color color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position pos = new Position(r, c);
                Piece piece = board.getPiece(pos);
                // NOTE: Requires all concrete piece classes to be implemented with proper getType()
                if (piece != null && piece.getColor() == color && piece.getType().equals("King")) {
                    return Optional.of(pos);
                }
            }
        }
        return Optional.empty(); // Should not happen in a valid game
    }

    /**
     * Determines if a specific position on the board is currently being attacked
     * by any piece of the opponent's color.
     */
    public static boolean isSquareAttacked(Board board, Position targetPos, Color attackingColor) {
        Color defendingColor = (attackingColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

        // Iterate through all 64 squares
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position piecePos = new Position(r, c);
                Piece piece = board.getPiece(piecePos);

                if (piece != null && piece.getColor() == attackingColor) {
                    // For every opponent's piece, calculate its *potential* moves (Strategy Pattern)
                    List<Move> potentialMoves = piece.getLegalMoves(board, piecePos);

                    // Check if any of these potential moves land on the target position
                    if (potentialMoves.stream().anyMatch(move -> move.end().equals(targetPos))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the King of the defending color is currently in check.
     */
    public static boolean isKingInCheck(Board board, Color defendingColor) {
        Color attackingColor = (defendingColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

        Optional<Position> kingPos = findKingPosition(board, defendingColor);

        if (kingPos.isEmpty()) {
            return false; // Should never happen
        }

        // Check if the King's position is attacked by the opponent's pieces
        return isSquareAttacked(board, kingPos.get(), attackingColor);
    }

    // NOTE: isCheckmate(Board, Color) is highly complex, requiring checking if *any*
    // legal move removes the check. We will implement the State transition logic first.
}