package com.jigsaw.game.api.entity;

import com.jigsaw.game.model.JigsawGame;
import com.jigsaw.game.model.Player;
import com.jigsaw.game.model.PuzzlePiece;
import com.jigsaw.game.state.SetupState;
import com.jigsaw.game.state.GameState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity for the JigsawGame (The State Context).
 * This entity is responsible for persistence mapping, NOT business logic.
 */
@Entity
@Table(name = "jigsaw_game")
@Getter
@Setter
public class JigsawGameEntity {

    @Id
    private UUID gameId;

    // Mapping for the State Pattern's current state (persisted as a String)
    private String currentState;

    private int boardWidth;
    private int boardHeight;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PuzzlePieceEntity> pieces = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlayerEntity> players = new ArrayList<>();

    // Production-level audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Version
    private Long version; // Optimistic Locking

    // Constructors...
    public JigsawGameEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.gameId = UUID.randomUUID();
    }

    // --- LLD CONVERSION (The Bridge) ---

    /**
     * Converts the JPA Entity back to the LLD Domain Model for business operations.
     */
    public JigsawGame toDomain(GameState initialState) {
        // NOTE: In a real system, you would use a dedicated Mapper service (e.g., MapStruct)
        // and a State Factory to map the 'currentState' string to the actual GameState object.

        JigsawGame domain = JigsawGame.builder()
                .gameId(this.gameId)
                .boardWidth(this.boardWidth)
                .boardHeight(this.boardHeight)
                // Pieces and Players require complex mapping from Entity list to Domain list
                .pieces(pieces.stream().map(PuzzlePieceEntity::toDomain).toList())
                .players(players.stream().map(PlayerEntity::toDomain).toList())
                .build();

        // This is the CRITICAL LLD step: Re-hydrating the State Context
        // Simplified: Assume for now we always start with SetupState if the entity is new.
        // In reality: Check 'currentState' string and map to the correct state singleton.
        domain.changeState(initialState);

        return domain;
    }

    /**
     * Converts the LLD Domain Model to the JPA Entity for saving.
     */
    public static JigsawGameEntity fromDomain(JigsawGame domain) {
        JigsawGameEntity entity = new JigsawGameEntity();
        entity.setGameId(domain.getGameId());
        entity.setBoardWidth(domain.getBoardWidth());
        entity.setBoardHeight(domain.getBoardHeight());

        // Map State Pattern: Save the state's class name.
        entity.setCurrentState(domain.getCurrentState().getClass().getSimpleName());

        // Map Pieces and Players: Ensure bidirectional relationship is set
        List<PuzzlePieceEntity> pieceEntities = domain.getPieces().stream()
                .map(p -> PuzzlePieceEntity.fromDomain(p, entity))
                .toList();

        List<PlayerEntity> playerEntities = domain.getPlayers().stream()
                .map(p -> PlayerEntity.fromDomain(p, entity))
                .toList();

        entity.setPieces(pieceEntities);
        entity.setPlayers(playerEntities);

        return entity;
    }
}