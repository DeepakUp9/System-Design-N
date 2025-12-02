package com.chess.game.core.model.pieces;

import com.chess.game.core.model.Color;
import com.chess.game.core.model.Piece;
import com.chess.game.core.strategy.impl.BishopMoveStrategy;

// Bishop (Context)
public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color, new BishopMoveStrategy()); // Strategy Injection
    }
    @Override
    public String getType() { return "Bishop"; }
}