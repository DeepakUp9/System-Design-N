package com.jigsaw.game.state;

import com.jigsaw.game.model.JigsawGame;
import com.jigsaw.game.model.BoardPosition;

import java.util.UUID;

/**
 * Concrete State: The game is over (solved or forfeited).
 * Business Rule: No further moves are allowed. Scoring and cleanup occur here.
 */
public class SolvedState implements GameState {

    public SolvedState() {
        // Production: Trigger the Observer Pattern (events) here to log final results,
        // update leaderboards, send notifications, etc. (Planned for Step 6/7)
        System.out.println("GAME OVER: Finalizing scores and auditing results.");
    }

    @Override
    public boolean handlePiecePlacement(JigsawGame context, UUID pieceId, BoardPosition position) {
        System.out.println("ERROR: Game is solved. No further pieces can be placed.");
        return false;
    }

    @Override
    public boolean handleGameEnd(JigsawGame context, UUID playerId) {
        System.out.println("ERROR: Game is already concluded.");
        return false;
    }

    @Override
    public boolean handleGameStart(JigsawGame context) {
        // Production: Could transition to a 'RESTARTING' state, but generally, a solved game instance is immutable.
        System.out.println("ERROR: Cannot start a game that is already solved. A new GameContext must be created.");
        return false;
    }
}