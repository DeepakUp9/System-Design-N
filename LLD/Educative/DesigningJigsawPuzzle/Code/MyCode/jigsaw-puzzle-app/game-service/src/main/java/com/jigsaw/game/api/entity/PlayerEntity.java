package com.jigsaw.game.api.entity;

import com.jigsaw.game.model.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * JPA Entity for Player.
 */
@Entity
@Table(name = "player")
@Getter
@Setter
public class PlayerEntity {

    @Id
    private UUID playerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private JigsawGameEntity game;

    private String username;
    private boolean isHost;
    private int score;

    // --- LLD CONVERSION (The Bridge) ---

    public Player toDomain() {
        return Player.builder()
                .playerId(this.playerId)
                .username(this.username)
                .isHost(this.isHost)
                .score(this.score)
                .build();
    }

    public static PlayerEntity fromDomain(Player domain, JigsawGameEntity gameEntity) {
        PlayerEntity entity = new PlayerEntity();
        entity.setPlayerId(domain.getPlayerId());
        entity.setGame(gameEntity);
        entity.setUsername(domain.getUsername());
        entity.setIsHost(domain.isHost());
        entity.setScore(domain.getScore());
        return entity;
    }
}