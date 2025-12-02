package com.chess.game.core.model.pieces;


import com.chess.game.core.model.Color;
import com.chess.game.core.model.Piece;

// Pawn (Context) - requires strategy below
public class Pawn extends Piece {
    public Pawn(Color color) {
        // NOTE: PawnMoveStrategy requires the Pawn itself to track its first move status
        super(color, new PawnMoveStrategy());
    }
    @Override
    public String getType() { return "Pawn"; }
}