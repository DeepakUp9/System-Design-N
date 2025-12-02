package com.chess.game.core.strategy;

import com.chess.game.core.model.Board;
import com.chess.game.core.model.Move;
import com.chess.game.core.model.Position;
import com.chess.game.core.model.Color;


import java.util.List;

/**
 * STRATEGY INTERFACE: Defines the contract for calculating legal moves for a specific piece type.
 * This decouples the move calculation logic from the Piece class.
 */
public interface MoveStrategy {

    /**
     * Calculates all potential legal moves for a piece of the given color at a specific position on the board.
     * Note: This calculation typically does NOT check if the move puts the King in check,
     * as that is a higher-level GAME LOGIC responsibility (often handled by the GameContext/Service).
     *
     * @param board The current state of the board.
     * @param currentPos The position of the piece attempting to move.
     * @param color The color of the piece.
     * @return A list of possible moves.
     */
    List<Move> calculateLegalMoves(Board board, Position currentPos, Color color);
}