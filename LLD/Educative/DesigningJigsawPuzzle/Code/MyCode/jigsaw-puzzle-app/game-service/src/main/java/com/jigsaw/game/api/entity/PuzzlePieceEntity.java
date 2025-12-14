package com.jigsaw.game.api.entity;

import com.jigsaw.game.model.PieceType;
import com.jigsaw.game.model.BoardPosition;
import com.jigsaw.game.model.PuzzlePiece;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * JPA Entity for PuzzlePiece (The Strategy Context).
 */
@Entity
@Table(name = "puzzle_piece")
@Getter
@Setter
public class PuzzlePieceEntity {

    @Id
    private UUID pieceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private JigsawGameEntity game;

    // Strategy Pattern Type (persisted as String)
    @Enumerated(EnumType.STRING)
    private PieceType pieceType;

    private int flatEdgeCount;

    private int rotation;
    private Integer currentRow;
    private Integer currentCol;

    // --- LLD CONVERSION (The Bridge) ---

    public PuzzlePiece toDomain() {
        return PuzzlePiece.builder()
                .pieceId(this.pieceId)
                .type(this.pieceType)
                .rotation(this.rotation)
                .flatEdgeCount(this.flatEdgeCount)
                .currentPosition(this.currentRow != null ? new BoardPosition(this.currentRow, this.currentCol) : null)
                .build();
        // NOTE: The matchingStrategy will be set in the JigsawGameService after retrieval.
    }

    public static PuzzlePieceEntity fromDomain(PuzzlePiece domain, JigsawGameEntity gameEntity) {
        PuzzlePieceEntity entity = new PuzzlePieceEntity();
        entity.setPieceId(domain.getPieceId());
        entity.setGame(gameEntity);
        entity.setPieceType(domain.getType());
        entity.setFlatEdgeCount(domain.getFlatEdgeCount());
        entity.setRotation(domain.getRotation());

        if (domain.getCurrentPosition() != null) {
            entity.setCurrentRow(domain.getCurrentPosition().getRow());
            entity.setCurrentCol(domain.getCurrentPosition().getColumn());
        }
        return entity;
    }
}