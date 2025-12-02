package com.chess.game.core.factory;

// NOTE: We would add Queen, Rook, Bishop, Knight, Pawn here

import com.chess.game.core.model.Color;
import com.chess.game.core.model.Piece;
import com.chess.game.core.model.pieces.King;

/**
 * FACTORY PATTERN: Centralizes the creation of Piece objects.
 * This ensures that the caller (e.g., Board setup logic) doesn't need to know
 * how to instantiate the specific piece or its associated strategy.
 */
public class PieceFactory {

    /**
     * Creates a new Piece instance based on type and color.
     * We use a static method as the Factory itself often doesn't need state.
     *
     * @param pieceType The string identifier for the piece.
     * @param color The color of the piece.
     * @return The instantiated Piece object with its MoveStrategy injected.
     */
    public static Piece createPiece(String pieceType, Color color) {
        // This switch statement maps the type string to the concrete Piece class
        return switch (pieceType.toLowerCase()) {
            case "king" -> new King(color);
            // case "queen" -> new Queen(color);
            // case "rook" -> new Rook(color);
            // ... other pieces would go here
            default -> throw new IllegalArgumentException("Unknown piece type: " + pieceType);
        };
    }

    /**
     * Utility method to set up the initial board configuration.
     * (We'll use this in the GameService.)
     */
    public static Piece[][] getInitialPiecePlacement() {
        Piece[][] setup = new Piece[8][8];

        // --- White Pieces (Row 0) ---
        setup[0][4] = createPiece("king", Color.WHITE);
        // setup[0][0] = createPiece("rook", Color.WHITE);
        // setup[0][1] = createPiece("knight", Color.WHITE);
        // ...

        // --- Black Pieces (Row 7) ---
        setup[7][4] = createPiece("king", Color.BLACK);
        // setup[7][0] = createPiece("rook", Color.BLACK);
        // setup[7][1] = createPiece("knight", Color.BLACK);
        // ...

        // NOTE: We only have the King implementation right now.

        return setup;
    }
}