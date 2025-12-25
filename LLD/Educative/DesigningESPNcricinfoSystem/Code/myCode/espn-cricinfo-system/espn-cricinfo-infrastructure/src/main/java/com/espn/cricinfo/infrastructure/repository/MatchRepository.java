package com.espn.cricinfo.infrastructure.repository;

import com.espn.cricinfo.domain.enums.MatchFormat;
import com.espn.cricinfo.infrastructure.entity.MatchEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Match entities.
 * Provides data access operations for cricket matches.
 */
@Repository
@CacheConfig(cacheNames = "matches")
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    /**
     * Find matches by format.
     */
    @Cacheable(key = "'format:' + #format")
    List<MatchEntity> findByFormat(MatchFormat format);

    /**
     * Find matches by status.
     */
    List<MatchEntity> findByStatus(MatchEntity.MatchStatus status);

    /**
     * Find matches for a specific team.
     */
    @Query("SELECT m FROM MatchEntity m WHERE m.team1.id = :teamId OR m.team2.id = :teamId")
    List<MatchEntity> findByTeamId(@Param("teamId") Long teamId);

    /**
     * Find matches at a specific venue.
     */
    List<MatchEntity> findByVenueId(Long venueId);

    /**
     * Find matches within a date range.
     */
    List<MatchEntity> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Find matches by tournament.
     */
    List<MatchEntity> findByTournamentId(Long tournamentId);

    /**
     * Find active matches (in progress or paused).
     */
    @Query("SELECT m FROM MatchEntity m WHERE m.status IN ('IN_PROGRESS', 'PAUSED')")
    @Cacheable(key = "'active'")
    List<MatchEntity> findActiveMatches();

    /**
     * Find completed matches with winner.
     */
    @Query("SELECT m FROM MatchEntity m WHERE m.status = 'COMPLETED' AND m.winner IS NOT NULL")
    List<MatchEntity> findCompletedMatchesWithWinner();

    /**
     * Find matches by team names (for search functionality).
     */
    @Query("SELECT m FROM MatchEntity m WHERE " +
           "LOWER(m.team1.name) LIKE LOWER(CONCAT('%', :teamName, '%')) OR " +
           "LOWER(m.team2.name) LIKE LOWER(CONCAT('%', :teamName, '%'))")
    List<MatchEntity> findByTeamNameContaining(@Param("teamName") String teamName);

    /**
     * Find recent matches with pagination.
     */
    Page<MatchEntity> findByStartTimeBeforeOrderByStartTimeDesc(LocalDateTime date, Pageable pageable);

    /**
     * Count matches by format and status.
     */
    @Query("SELECT COUNT(m) FROM MatchEntity m WHERE m.format = :format AND m.status = :status")
    long countByFormatAndStatus(@Param("format") MatchFormat format,
                               @Param("status") MatchEntity.MatchStatus status);

    /**
     * Find matches with high scoring (for analytics).
     */
    @Query("SELECT m FROM MatchEntity m WHERE " +
           "EXISTS (SELECT i FROM InningsEntity i WHERE i.match = m AND i.totalRuns >= :minRuns)")
    List<MatchEntity> findHighScoringMatches(@Param("minRuns") int minRuns);

    /**
     * Find matches that ended in tie or no result.
     */
    @Query("SELECT m FROM MatchEntity m WHERE " +
           "m.status = 'COMPLETED' AND (m.winner IS NULL OR m.resultType IN ('TIE', 'NO_RESULT'))")
    List<MatchEntity> findMatchesWithoutClearWinner();

    /**
     * Get match statistics summary.
     */
    @Query("SELECT " +
           "COUNT(m) as totalMatches, " +
           "SUM(CASE WHEN m.winner = m.team1 THEN 1 ELSE 0 END) as team1Wins, " +
           "SUM(CASE WHEN m.winner = m.team2 THEN 1 ELSE 0 END) as team2Wins, " +
           "AVG((SELECT COALESCE(SUM(i.totalRuns), 0) FROM InningsEntity i WHERE i.match = m)) as avgRunsPerMatch " +
           "FROM MatchEntity m WHERE m.startTime BETWEEN :startDate AND :endDate")
    Object[] getMatchStatisticsSummary(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Find matches with specific weather conditions.
     */
    List<MatchEntity> findByWeatherCondition(String weatherCondition);

    /**
     * Find matches that were abandoned.
     */
    List<MatchEntity> findByStatus(MatchEntity.MatchStatus status);

    /**
     * Find matches by umpire.
     */
    @Query("SELECT m FROM MatchEntity m WHERE " +
           "m.umpire1Name = :umpireName OR m.umpire2Name = :umpireName OR " +
           "m.thirdUmpireName = :umpireName OR m.matchRefereeName = :umpireName")
    List<MatchEntity> findByUmpireName(@Param("umpireName") String umpireName);

    /**
     * Find matches with DRS reviews.
     */
    @Query("SELECT DISTINCT m FROM MatchEntity m " +
           "JOIN m.balls b WHERE b.reviewed = true")
    List<MatchEntity> findMatchesWithDRSReviews();

    /**
     * Get live match updates (for real-time features).
     */
    @Query("SELECT m FROM MatchEntity m WHERE m.status = 'IN_PROGRESS' " +
           "ORDER BY m.updatedAt DESC")
    @Cacheable(key = "'live'", unless = "#result.isEmpty()")
    List<MatchEntity> findLiveMatches();

    /**
     * Find matches by date and venue for scheduling.
     */
    @Query("SELECT m FROM MatchEntity m WHERE " +
           "DATE(m.startTime) = DATE(:date) AND m.venue.id = :venueId")
    Optional<MatchEntity> findByDateAndVenue(@Param("date") LocalDateTime date,
                                            @Param("venueId") Long venueId);
}
