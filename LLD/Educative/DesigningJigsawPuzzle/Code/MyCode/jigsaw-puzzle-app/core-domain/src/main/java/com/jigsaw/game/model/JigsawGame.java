package com.jigsaw.game.model;

import com.jigsaw.game.state.GameState;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * The core Entity representing the entire game instance (The State Context).
 * CRITICAL: It holds the current GameState and delegates all game-flow actions to it.
 */
@Data
@Builder
public class JigsawGame {

    // Production-level identification
    @Builder.Default
    private UUID gameId = UUID.randomUUID();

    @NonNull
    private List<Player> players;

    @NonNull
    private List<PuzzlePiece> pieces;

    // The LLD State Context Reference (Crucial for the State Pattern)
    private GameState currentState;

    // Board structure (for simple 2D representation)
    private final int boardWidth;
    private final int boardHeight;

    /**
     * State Pattern Method: Delegates game actions to the current state object.
     */
    public boolean placePiece(UUID pieceId, BoardPosition position) {
        if (currentState == null) {
            throw new IllegalStateException("Game state not initialized.");
        }
        // The GameContext delegates the operation, ensuring only valid actions for the current state are possible.
        return currentState.handlePiecePlacement(this, pieceId, position);
    }

    /**
     * Allows the current state to change the context's state.
     * This is how the game progresses from Setup -> InProgress -> Solved.
     * @param newState The new GameState object.
     */
    public void changeState(GameState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("Cannot change to a null state.");
        }
        System.out.printf("Game %s transitioning from %s to %s%n",
                gameId,
                currentState != null ? currentState.getClass().getSimpleName() : "INITIALIZED",
                newState.getClass().getSimpleName());
        this.currentState = newState;
    }
}