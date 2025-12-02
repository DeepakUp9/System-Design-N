package com.chess.game.core.model.pieces;

import com.chess.game.core.model.Color;
import com.chess.game.core.model.Piece;
import com.chess.game.core.strategy.impl.KnightMoveStrategy;

// Knight (Context) - requires strategy below
public class Knight extends Piece {
    public Knight(Color color) {
        super(color, new KnightMoveStrategy()); // Strategy Injection
    }
    @Override
    public String getType() { return "Knight"; }
}