package com.espn.cricinfo.infrastructure.repository;

import com.espn.cricinfo.infrastructure.entity.PlayerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Player entities.
 * Provides comprehensive data access operations for cricket players.
 */
@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    /**
     * Find player by name.
     */
    Optional<PlayerEntity> findByName(String name);

    /**
     * Find player by full name.
     */
    Optional<PlayerEntity> findByFullName(String fullName);

    /**
     * Find players by role.
     */
    List<PlayerEntity> findByRole(String role);

    /**
     * Find players by role with pagination.
     */
    Page<PlayerEntity> findByRole(String role, Pageable pageable);

    /**
     * Find players by team.
     */
    List<PlayerEntity> findByTeamId(Long teamId);

    /**
     * Find players by team with pagination.
     */
    Page<PlayerEntity> findByTeamId(Long teamId, Pageable pageable);

    /**
     * Find players by nationality.
     */
    List<PlayerEntity> findByNationality(String nationality);

    /**
     * Find players by age range.
     */
    @Query("SELECT p FROM PlayerEntity p WHERE p.age BETWEEN :minAge AND :maxAge")
    List<PlayerEntity> findByAgeBetween(@Param("minAge") int minAge, @Param("maxAge") int maxAge);

    /**
     * Find active players.
     */
    List<PlayerEntity> findByIsActiveTrue();

    /**
     * Find retired players.
     */
    @Query("SELECT p FROM PlayerEntity p WHERE p.retirementDate IS NOT NULL")
    List<PlayerEntity> findRetiredPlayers();

    /**
     * Find players born after date.
     */
    List<PlayerEntity> findByDateOfBirthAfter(LocalDate date);

    /**
     * Find players by name containing pattern.
     */
    @Query("SELECT p FROM PlayerEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<PlayerEntity> findByNameContaining(@Param("name") String name);

    /**
     * Find captains of teams.
     */
    @Query("SELECT p FROM PlayerEntity p WHERE p.id IN " +
           "(SELECT t.captain.id FROM TeamEntity t WHERE t.captain IS NOT NULL)")
    List<PlayerEntity> findTeamCaptains();

    /**
     * Find wicket keepers.
     */
    @Query("SELECT p FROM PlayerEntity p WHERE p.role = 'WICKET_KEEPER'")
    List<PlayerEntity> findWicketKeepers();

    /**
     * Find all-rounders.
     */
    @Query("SELECT p FROM PlayerEntity p WHERE p.role = 'ALL_ROUNDER'")
    List<PlayerEntity> findAllRounders();

    /**
     * Get player statistics summary by role.
     */
    @Query("SELECT p.role, COUNT(p), AVG(p.age) FROM PlayerEntity p GROUP BY p.role")
    List<Object[]> getPlayerStatisticsByRole();

    /**
     * Find players by batting style.
     */
    List<PlayerEntity> findByBattingStyle(String battingStyle);

    /**
     * Find players by bowling style.
     */
    List<PlayerEntity> findByBowlingStyle(String bowlingStyle);

    /**
     * Find players with jersey numbers.
     */
    @Query("SELECT p FROM PlayerEntity p WHERE p.jerseyNumber IS NOT NULL")
    List<PlayerEntity> findPlayersWithJerseyNumbers();

    /**
     * Count players by nationality.
     */
    @Query("SELECT p.nationality, COUNT(p) FROM PlayerEntity p GROUP BY p.nationality ORDER BY COUNT(p) DESC")
    List<Object[]> countPlayersByNationality();

    /**
     * Find players eligible for retirement (age 35+).
     */
    @Query("SELECT p FROM PlayerEntity p WHERE p.age >= 35 AND p.retirementDate IS NULL")
    List<PlayerEntity> findPlayersEligibleForRetirement();

    /**
     * Find young talents (age 18-21).
     */
    @Query("SELECT p FROM PlayerEntity p WHERE p.age BETWEEN 18 AND 21")
    List<PlayerEntity> findYoungTalents();

    /**
     * Search players by multiple criteria.
     */
    @Query("SELECT p FROM PlayerEntity p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:role IS NULL OR p.role = :role) AND " +
           "(:nationality IS NULL OR p.nationality = :nationality) AND " +
           "(:teamId IS NULL OR p.team.id = :teamId)")
    List<PlayerEntity> searchPlayers(@Param("name") String name,
                                   @Param("role") String role,
                                   @Param("nationality") String nationality,
                                   @Param("teamId") Long teamId,
                                   Pageable pageable);
}
