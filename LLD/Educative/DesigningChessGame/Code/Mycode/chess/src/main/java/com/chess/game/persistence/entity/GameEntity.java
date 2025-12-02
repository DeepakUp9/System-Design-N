package com.chess.game.persistence.entity;

import com.chess.game.core.model.Board;
import com.chess.game.core.model.Color;
import com.chess.game.core.state.impl.InProgressState;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

/**
 * JPA ENTITY: Represents a persisted Chess Game record in the PostgreSQL database.
 */
@Entity
@Table(name = "chess_game")
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 1. Board State: Storing the complex Board as JSON in PostgreSQL
    // This simplifies mapping since Board contains Piece objects (which have MoveStrategies).
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "board_state", columnDefinition = "jsonb")
    private Board board;

    // 2. Game Metadata
    @Enumerated(EnumType.STRING)
    @Column(name = "active_player", nullable = false)
    private Color activePlayer;

    @Column(name = "game_state_name", nullable = false)
    private String gameStateName; // Stores the name of the current State (e.g., "InProgressState")

    public GameEntity() {
        // Default constructor for JPA
    }

    // Static factory method for creation
    public static GameEntity createNew(Board initialBoard) {
        GameEntity entity = new GameEntity();
        entity.setBoard(initialBoard);
        entity.setActivePlayer(Color.WHITE);
        entity.setGameStateName(new InProgressState().getClass().getSimpleName());
        return entity;
    }

    // --- Getters and Setters (omitted for brevity, but required by JPA) ---

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    public Color getActivePlayer() { return activePlayer; }
    public void setActivePlayer(Color activePlayer) { this.activePlayer = activePlayer; }
    public String getGameStateName() { return gameStateName; }
    public void setGameStateName(String gameStateName) { this.gameStateName = gameStateName; }
}
