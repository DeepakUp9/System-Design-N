package com.chess.game.core.state;

import com.chess.game.core.model.Board;
import com.chess.game.core.state.impl.InProgressState;
import com.chess.game.core.model.Color;
import com.chess.game.core.model.Move;
import com.chess.game.core.model.Piece;

/**
 * CONTEXT: The main class that maintains the game state.
 * It holds a reference to a ConcreteState object and delegates state-specific
 * behavior to it.
 */
public class GameContext {

    private GameState currentState; // Reference to the current State (e.g., InProgressState)
    private Board board;
    private Color activePlayer; // Tracks whose turn it is

    public GameContext(Board board) {
        this.board = board;
        this.activePlayer = Color.WHITE; // White always starts
        // Initialize the Context with the default starting State
        this.currentState = new InProgressState();
    }

    // This method is called by the outside world (Spring Boot Service)
    public void submitMove(Move move) {
        System.out.println("Current State: " + currentState.getStatus());
        // Delegate the action to the current state object, which returns the next state.
        GameState nextState = currentState.handleMove(this, move);
        setCurrentState(nextState);
        System.out.println("New State: " + currentState.getStatus());
    }

    // State Pattern Setters/Getters
    public void setCurrentState(GameState newState) {
        this.currentState = newState;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    // Game Logic Getters/Setters
    public Board getBoard() {
        return board;
    }

    public Color getActivePlayer() {
        return activePlayer;
    }

    public void switchActivePlayer() {
        this.activePlayer = (activePlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    // Helper method to check if a move is legal (will be used by InProgressState)
    public boolean isLegalMoveForActivePlayer(Move move) {
        Piece piece = board.getPiece(move.start());

        // 1. Check if the piece exists and belongs to the active player
        if (piece == null || piece.getColor() != activePlayer) {
            return false;
        }

        // 2. Check if the move is in the piece's calculated legal moves (Strategy Pattern)
        return piece.getLegalMoves(board, move.start()).contains(move);

        // FUTURE: Also check if the move removes the king from check, etc.
    }
}
