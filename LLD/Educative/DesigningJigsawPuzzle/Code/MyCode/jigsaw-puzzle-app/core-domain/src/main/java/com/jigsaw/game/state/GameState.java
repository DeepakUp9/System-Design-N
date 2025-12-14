package com.jigsaw.game.state;

import com.jigsaw.game.model.JigsawGame;
import com.jigsaw.game.model.BoardPosition;

import java.util.UUID;

/**
 * The State Interface: Defines methods for game actions.
 * All concrete states MUST implement this interface, enforcing the LLD structure.
 */
public interface GameState {

    /**
     * Handles the attempt to place a piece. Logic varies based on state (e.g., only allowed
     * in IN_PROGRESS state).
     * @param context The JigsawGame instance (the Context)
     * @param pieceId The piece being placed.
     * @param position The position on the board.
     * @return true if the placement was successful and state potentially changed.
     */
    boolean handlePiecePlacement(JigsawGame context, UUID pieceId, BoardPosition position);

    /**
     * Handles a player forfeiting or manually ending the game prematurely.
     * @param context The JigsawGame instance.
     * @param playerId The player performing the action.
     * @return true if the action was successful and state changed (usually to SOLVED).
     */
    boolean handleGameEnd(JigsawGame context, UUID playerId);

    /**
     * Handles initiating the game setup (e.g., shuffling pieces, validating board size).
     * @param context The JigsawGame instance.
     * @return true if the transition to the next state (e.g., InProgress) is possible.
     */
    boolean handleGameStart(JigsawGame context);
}