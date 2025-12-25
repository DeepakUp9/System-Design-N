package com.espn.cricinfo.infrastructure.repository;

import com.espn.cricinfo.infrastructure.entity.TeamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Team entities.
 * Provides data access operations for cricket teams.
 */
@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    /**
     * Find team by name.
     */
    Optional<TeamEntity> findByName(String name);

    /**
     * Find team by short name.
     */
    Optional<TeamEntity> findByShortName(String shortName);

    /**
     * Find teams by country.
     */
    List<TeamEntity> findByCountry(String country);

    /**
     * Find teams by country with pagination.
     */
    Page<TeamEntity> findByCountry(String country, Pageable pageable);

    /**
     * Find teams containing name pattern.
     */
    @Query("SELECT t FROM TeamEntity t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<TeamEntity> findByNameContaining(@Param("name") String name);

    /**
     * Find teams by player.
     */
    @Query("SELECT t FROM TeamEntity t JOIN t.players p WHERE p.id = :playerId")
    Optional<TeamEntity> findByPlayerId(@Param("playerId") Long playerId);

    /**
     * Find teams with captain.
     */
    @Query("SELECT t FROM TeamEntity t WHERE t.captain IS NOT NULL")
    List<TeamEntity> findTeamsWithCaptain();

    /**
     * Find teams without captain.
     */
    @Query("SELECT t FROM TeamEntity t WHERE t.captain IS NULL")
    List<TeamEntity> findTeamsWithoutCaptain();

    /**
     * Get team statistics summary.
     */
    @Query("SELECT " +
           "COUNT(t) as totalTeams, " +
           "AVG(t.totalMatchesPlayed) as avgMatches, " +
           "SUM(t.totalMatchesWon) as totalWins, " +
           "MAX(t.totalMatchesWon) as maxWins " +
           "FROM TeamEntity t")
    Object[] getTeamStatisticsSummary();

    /**
     * Find top performing teams by win percentage.
     */
    @Query("SELECT t FROM TeamEntity t WHERE t.totalMatchesPlayed >= :minMatches " +
           "ORDER BY (t.totalMatchesWon * 1.0 / t.totalMatchesPlayed) DESC")
    List<TeamEntity> findTopTeamsByWinPercentage(@Param("minMatches") int minMatches, Pageable pageable);

    /**
     * Count teams by country.
     */
    @Query("SELECT t.country, COUNT(t) FROM TeamEntity t GROUP BY t.country")
    List<Object[]> countTeamsByCountry();

    /**
     * Find teams with most players.
     */
    @Query("SELECT t FROM TeamEntity t ORDER BY SIZE(t.players) DESC")
    List<TeamEntity> findTeamsByPlayerCount(Pageable pageable);
}
