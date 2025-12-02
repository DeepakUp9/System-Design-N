package com.chess.game.core.model.pieces;

import com.chess.game.core.model.Color;
import com.chess.game.core.model.Piece;
import com.chess.game.core.strategy.impl.BishopMoveStrategy;
import com.chess.game.core.strategy.impl.QueenMoveStrategy;
import com.chess.game.core.strategy.impl.RookMoveStrategy;

// Rook (Context)
public class Rook extends Piece {
    public Rook(Color color) {
        super(color, new RookMoveStrategy()); // Strategy Injection
    }
    @Override
    public String getType() { return "Rook"; }
}



