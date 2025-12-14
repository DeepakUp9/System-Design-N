package com.jigsaw.game.state;

import com.jigsaw.game.model.JigsawGame;
import com.jigsaw.game.model.BoardPosition;

import java.util.UUID;

/**
 * Concrete State: The game is being initialized (players joining, pieces shuffled).
 * Business Rule: Pieces cannot be placed, and the game cannot be ended in this state.
 */
public class SetupState implements GameState {

    @Override
    public boolean handlePiecePlacement(JigsawGame context, UUID pieceId, BoardPosition position) {
        System.out.println("ERROR: Cannot place pieces during Setup. Game must be started first.");
        // The LLD pattern ensures invalid actions for a state are blocked cleanly.
        return false;
    }

    @Override
    public boolean handleGameEnd(JigsawGame context, UUID playerId) {
        System.out.println("LOG: Game cancelled by player " + playerId + " during setup.");
        // Production: This usually leads to a TERMINATED or CANCELLED state (which we could add later).
        return true;
    }

    @Override
    public boolean handleGameStart(JigsawGame context) {
        // Production validation: Check if enough players, if all pieces are validated (using Strategy), etc.
        if (context.getPlayers().size() < 2) {
            System.out.println("SETUP_ERROR: Minimum two players required to start the game.");
            return false;
        }

        // LLD Action: Change the Context's state to the next stage.
        System.out.println("SETUP SUCCESS: Game is starting. Transitioning to In-Progress state.");
        context.changeState(new InProgressState());
        return true;
    }
}