package com.jigsaw.game.api.repository;

import com.jigsaw.game.api.entity.JigsawGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA Repository for the Jigsaw Game Entity.
 * Handles persistence operations.
 */
@Repository
public interface GameRepository extends JpaRepository<JigsawGameEntity, UUID> {

    // Spring Data automatically handles simple queries (e.g., findById, save)

    // Production note: Add complex query methods here as needed, e.g.:
    // List<JigsawGameEntity> findByCurrentState(String state);
}