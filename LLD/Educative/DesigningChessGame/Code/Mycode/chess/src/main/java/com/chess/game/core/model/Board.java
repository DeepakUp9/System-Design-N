package com.chess.game.core.model;
import com.chess.game.core.model.Move;
import com.chess.game.core.model.Piece;
import com.chess.game.core.model.Position;

/**
 * Represents the 8x8 Chess Board.
 * This is a critical component that stores the game state.
 */
public class Board {

    private final Piece[][] squares = new Piece[8][8];

    // Simple constructor for an empty board (setup logic will be in a Factory/Initializer)
    public Board() {
        // Initialize with nulls or empty squares
    }

    public Piece getPiece(Position pos) {
        if (pos.isValid()) {
            return squares[pos.row()][pos.col()];
        }
        return null; // Position is off the board
    }

    public void setPiece(Position pos, Piece piece) {
        if (pos.isValid()) {
            squares[pos.row()][pos.col()] = piece;
        }
    }

    /**
     * Executes a move on the board, updating the piece positions.
     * NOTE: This method does NOT check legality (that's the MoveStrategy's job).
     * It just executes the state change.
     */
    public void executeMove(Move move) {
        Piece pieceToMove = getPiece(move.start());
        if (pieceToMove != null) {
            // Place the piece at the new position (handles capture by overwriting)
            setPiece(move.end(), pieceToMove);
            // Clear the starting position
            setPiece(move.start(), null);
        }
    }

    // Utility to determine if a position is occupied
    public boolean isOccupied(Position pos) {
        return getPiece(pos) != null;
    }
}