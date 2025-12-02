package com.chess.game.core.state.impl;

import com.chess.game.core.model.Board;
import com.chess.game.core.model.Move;
import com.chess.game.core.state.GameContext;
import com.chess.game.core.state.GameState;
import com.chess.game.core.util.GameUtil;

/**
 * CONCRETE STATE: The active player's King is in check.
 * The primary rule here is: a move is only valid if it resolves the check.
 */
public class CheckState implements GameState {

    @Override
    public GameState handleMove(GameContext context, Move move) {

        // 1. Basic Validation (Is it the right piece? Is it a legal piece move?)
        if (!context.isLegalMoveForActivePlayer(move)) {
            System.out.println("Invalid move attempted (piece/target rule).");
            return this; // Stay in CheckState
        }

        // --- CRITICAL CHECK STATE LOGIC ---
        // 2. TEMPORARY EXECUTION: Simulate the move on a cloned board to see the result
        // For production, the Board class needs a deep-copy method. We simulate for now:
        Board tempBoard = context.getBoard(); // Using original for now, needs cloning for safety

        // Execute the move temporarily (conceptually)
        tempBoard.executeMove(move);

        // 3. CHECK RESOLUTION: Does the move resolve the check?
        if (GameUtil.isKingInCheck(tempBoard, context.getActivePlayer())) {
            // Revert the temporary move (CRITICAL: needs proper clone/undo)
            // tempBoard.undoMove(move);

            System.out.println("Move rejected: Does not resolve check.");
            return this; // Move rejected, stay in CheckState
        }

        // --- Move is valid (removes check or blocks attack) ---
        // 4. Finalize Move and Check Next State
        context.getBoard().executeMove(move); // If using a temporary board, this line executes it on the real one.

        // 5. Switch Player and Check opponent's state
        context.switchActivePlayer();

        // Future: Check if the new active player (the opponent) is in checkmate/stalemate
        /*
        if (GameUtil.isCheckmate(context.getBoard(), context.getActivePlayer())) {
            return new CheckmateState(context.getOppositePlayer());
        } else if (GameUtil.isKingInCheck(context.getBoard(), context.getActivePlayer())) {
            // This should ideally not happen if the move was valid, but acts as a safeguard.
            return new CheckState();
        }
        */

        // If the game continues, transition to the normal state
        return new InProgressState();
    }

    @Override
    public boolean isGameOver() { return false; }

    @Override
    public String getStatus() { return "Check!"; }
}