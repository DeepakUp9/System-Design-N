package com.chess.game.persistence.repository;

import com.chess.game.persistence.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * REPOSITORY: Standard Spring Data JPA interface for CRUD operations.
 */
@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {
    // Spring generates all implementation methods (save, findById, etc.) automatically.
}