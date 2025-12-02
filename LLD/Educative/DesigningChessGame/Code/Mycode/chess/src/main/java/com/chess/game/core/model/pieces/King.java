package com.chess.game.core.model.pieces;

import com.chess.game.core.model.Color;
import com.chess.game.core.model.Piece;
import com.chess.game.core.strategy.impl.KingMoveStrategy;

/**
 * Concrete Piece: King.
 * This class instantiates and holds its specific MoveStrategy.
 */
public class King extends Piece {

    public King(Color color) {
        // Inject the specific strategy (KingMoveStrategy) into the base Piece class (Context)
        super(color, new KingMoveStrategy());
    }

    @Override
    public String getType() {
        return "King";
    }
}