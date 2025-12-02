package com.chess.game.core.state.impl;

import com.chess.game.core.model.Color;
import com.chess.game.core.model.Move;
import com.chess.game.core.state.GameContext;
import com.chess.game.core.state.GameState;
import com.chess.game.core.util.GameUtil;

/**
 * CONCRETE STATE: The game is running, and players are taking turns.
 */
public class InProgressState implements GameState {

    @Override
    public GameState handleMove(GameContext context, Move move) {
        // --- 1. Basic Validation (Is it the right piece?)
        if (!context.isLegalMoveForActivePlayer(move)) {
            System.out.println("Invalid move attempted by " + context.getActivePlayer() + ": " + move);
            return this; // Stay in the current state, move rejected.
        }

        // --- 2. Execution
        context.getBoard().executeMove(move);

        // 3. Switch Player *before* checking the opponent's state
        context.switchActivePlayer();
        Color nextActivePlayer = context.getActivePlayer();

        // --- NEW TRANSITION LOGIC ---
        // 4. Check opponent's (the new active player's) King status

        if (GameUtil.isKingInCheck(context.getBoard(), nextActivePlayer)) {
            // NOTE: Must differentiate between Check and Checkmate here (complex logic)

            // Placeholder: Assume Checkmate if the King is attacked AND no legal moves remain
            // if (GameUtil.isCheckmate(context.getBoard(), nextActivePlayer)) {
            //     return new CheckmateState(context.getOppositePlayer());
            // }

            // If not checkmate, transition to CheckState
            return new CheckState();
        }

        // If no game-ending conditions, stay in the InProgressState
        return this;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public String getStatus() {
        return "Game In Progress";
    }
}
