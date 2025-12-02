package com.chess.game.core.model.pieces;

import com.chess.game.core.model.Color;
import com.chess.game.core.model.Piece;
import com.chess.game.core.strategy.impl.QueenMoveStrategy;

// Queen (Context)
public class Queen extends Piece {
    public Queen(Color color) {
        super(color, new QueenMoveStrategy()); // Strategy Injection
    }
    @Override
    public String getType() { return "Queen"; }
}