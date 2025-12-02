package com.chess.game.core.model;


import com.chess.game.core.strategy.MoveStrategy;

import java.util.List;

/**
 * The base class for all chess pieces.
 * This is the CONTEXT in the Strategy Pattern.
 */
public abstract class Piece {

    protected Color color;
    protected MoveStrategy moveStrategy; // Reference to the Strategy

    public Piece(Color color, MoveStrategy moveStrategy) {
        this.color = color;
        this.moveStrategy = moveStrategy;
    }

    // Delegation to the Strategy
    public List<Move> getLegalMoves(Board board, Position currentPos) {
        // Delegate the calculation to the specific strategy instance
        return moveStrategy.calculateLegalMoves(board, currentPos, this.color);
    }

    public Color getColor() {
        return color;
    }

    // Optional: Abstract method to get the type name (for Factory/Display)
    public abstract String getType();

    // Standard getters/setters omitted for brevity
}
