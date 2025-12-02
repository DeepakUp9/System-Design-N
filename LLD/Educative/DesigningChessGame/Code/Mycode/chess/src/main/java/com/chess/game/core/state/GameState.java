package com.chess.game.core.state;


import com.chess.game.core.model.Move;

/**
 * STATE INTERFACE: Defines the methods that handle game actions.
 * Each concrete state will implement this interface to define state-specific behavior.
 */
public interface GameState {

    /**
     * Handles a player's move attempt.
     * The implementation in each concrete state will vary:
     * - In Progress: Validates move, executes it, then checks for Check/Checkmate, and switches turn/state.
     * - Checkmate: Throws an exception or returns an error message.
     *
     * @param context The GameContext holding the current board and player.
     * @param move The move attempted by the current player.
     * @return The new GameState (e.g., InProgress, Check, Checkmate).
     */
    GameState handleMove(GameContext context, Move move);

    /**
     * @return A boolean indicating if the game has concluded in this state.
     */
    boolean isGameOver();

    /**
     * @return A string representation of the current state (for logging/display).
     */
    String getStatus();
}