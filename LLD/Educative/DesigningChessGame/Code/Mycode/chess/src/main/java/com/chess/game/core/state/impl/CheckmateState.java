package com.chess.game.core.state.impl;

import com.chess.game.core.model.Color;
import com.chess.game.core.model.Move;
import com.chess.game.core.state.GameContext;
import com.chess.game.core.state.GameState;

/**
 * CONCRETE STATE: The game is over, the King has been checkmated.
 */
public class CheckmateState implements GameState {

    private final Color winningColor;

    public CheckmateState(Color winningColor) {
        this.winningColor = winningColor;
    }

    @Override
    public GameState handleMove(GameContext context, Move move) {
        // State Pattern: No actions are allowed in a terminal state
        throw new IllegalStateException("Game is over. " + winningColor + " won by Checkmate.");
    }

    @Override
    public boolean isGameOver() { return true; }

    @Override
    public String getStatus() { return "Checkmate! " + winningColor + " wins."; }
}