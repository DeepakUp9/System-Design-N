package com.jigsaw.game.state;

import com.jigsaw.game.model.JigsawGame;
import com.jigsaw.game.model.PuzzlePiece;
import com.jigsaw.game.model.BoardPosition;

import java.util.UUID;

/**
 * Concrete State: The game is actively being played.
 * Business Rule: Piece placement is allowed, and victory conditions must be checked after every move.
 */
public class InProgressState implements GameState {

    @Override
    public boolean handlePiecePlacement(JigsawGame context, UUID pieceId, BoardPosition position) {
        // 1. Find the piece (using Java Stream or Map lookup in production)
        PuzzlePiece pieceToPlace = context.getPieces().stream()
                .filter(p -> p.getPieceId().equals(pieceId))
                .findFirst()
                .orElse(null);

        if (pieceToPlace == null) {
            System.out.println("ERROR: Piece not found.");
            return false;
        }

        // 2. Delegate the core placement logic to the Strategy Context (PuzzlePiece)
        // Note: The 'neighboringPieces' object would contain complex data about surrounding pieces.
        boolean isValid = pieceToPlace.place(position, null); // Using null for neighboringPieces placeholder

        if (isValid) {
            System.out.printf("SUCCESS: Piece %s placed at (%d, %d).%n",
                    pieceId.toString().substring(0, 4), position.getRow(), position.getColumn());

            // 3. Post-placement check for state transition
            if (checkVictoryCondition(context)) {
                context.changeState(new SolvedState());
            }
        } else {
            System.out.println("VALIDATION FAILED: Piece placement invalid by Strategy pattern.");
        }

        return isValid;
    }

    /**
     * Production-level logic to check if the entire puzzle has been solved.
     */
    private boolean checkVictoryCondition(JigsawGame context) {
        // Simplified: Check if all pieces have a non-null position.
        // Real logic: Check if all pieces are placed AND all connections are correct.
        long placedPieces = context.getPieces().stream().filter(p -> p.getCurrentPosition() != null).count();
        return placedPieces == context.getPieces().size();
    }


    @Override
    public boolean handleGameEnd(JigsawGame context, UUID playerId) {
        System.out.println("LOG: Player " + playerId + " is forfeiting the game.");
        // LLD Action: Change state to Solved/Terminated and calculate penalties/scores.
        context.changeState(new SolvedState());
        return true;
    }

    @Override
    public boolean handleGameStart(JigsawGame context) {
        System.out.println("ERROR: Game is already in progress.");
        return false;
    }
}