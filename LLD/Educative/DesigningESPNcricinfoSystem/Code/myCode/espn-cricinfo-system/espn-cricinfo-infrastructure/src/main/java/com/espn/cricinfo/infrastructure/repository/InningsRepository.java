package com.espn.cricinfo.infrastructure.repository;

import com.espn.cricinfo.infrastructure.entity.InningsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Innings entities.
 * Provides data access operations for cricket innings.
 */
@Repository
public interface InningsRepository extends JpaRepository<InningsEntity, Long> {

    /**
     * Find innings by match.
     */
    List<InningsEntity> findByMatchId(Long matchId);

    /**
     * Find innings by match and innings number.
     */
    Optional<InningsEntity> findByMatchIdAndInningsNumber(Long matchId, Integer inningsNumber);

    /**
     * Find innings by batting team.
     */
    List<InningsEntity> findByBattingTeamId(Long battingTeamId);

    /**
     * Find innings by bowling team.
     */
    List<InningsEntity> findByBowlingTeamId(Long bowlingTeamId);

    /**
     * Find completed innings.
     */
    List<InningsEntity> findByIsCompletedTrue();

    /**
     * Find declared innings.
     */
    List<InningsEntity> findByIsDeclaredTrue();

    /**
     * Find innings with target.
     */
    @Query("SELECT i FROM InningsEntity i WHERE i.targetRuns IS NOT NULL")
    List<InningsEntity> findInningsWithTarget();

    /**
     * Find high-scoring innings.
     */
    @Query("SELECT i FROM InningsEntity i WHERE i.totalRuns >= :minRuns")
    List<InningsEntity> findHighScoringInnings(@Param("minRuns") Integer minRuns);

    /**
     * Find all-out innings.
     */
    @Query("SELECT i FROM InningsEntity i WHERE i.isCompleted = true AND i.totalWickets = 10")
    List<InningsEntity> findAllOutInnings();

    /**
     * Find innings by run rate range.
     */
    @Query("SELECT i FROM InningsEntity i WHERE i.totalRuns / i.totalOvers BETWEEN :minRate AND :maxRate")
    List<InningsEntity> findInningsByRunRateRange(@Param("minRate") Double minRate,
                                                 @Param("maxRate") Double maxRate);

    /**
     * Get innings statistics summary.
     */
    @Query("SELECT " +
           "COUNT(i) as totalInnings, " +
           "AVG(i.totalRuns) as avgRuns, " +
           "MAX(i.totalRuns) as maxRuns, " +
           "AVG(i.totalWickets) as avgWickets, " +
           "SUM(i.totalRuns) as totalRunsAllInnings " +
           "FROM InningsEntity i WHERE i.isCompleted = true")
    Object[] getInningsStatisticsSummary();

    /**
     * Find innings with successful chases.
     */
    @Query("SELECT i FROM InningsEntity i WHERE " +
           "i.targetRuns IS NOT NULL AND i.totalRuns >= i.targetRuns AND i.isCompleted = true")
    List<InningsEntity> findSuccessfulChases();

    /**
     * Find innings with failed chases.
     */
    @Query("SELECT i FROM InningsEntity i WHERE " +
           "i.targetRuns IS NOT NULL AND i.totalRuns < i.targetRuns AND i.isCompleted = true")
    List<InningsEntity> findFailedChases();

    /**
     * Find powerplay innings (first 10 overs).
     */
    @Query("SELECT i FROM InningsEntity i WHERE i.totalOvers <= 10")
    List<InningsEntity> findPowerplayInnings();

    /**
     * Find death overs innings (last 5 overs).
     */
    @Query("SELECT i FROM InningsEntity i WHERE i.totalOvers >= :maxOvers - 5 AND i.match.format != 'TEST'")
    List<InningsEntity> findDeathOversInnings(@Param("maxOvers") Integer maxOvers);

    /**
     * Count innings by result type.
     */
    @Query("SELECT " +
           "SUM(CASE WHEN i.isCompleted = true AND i.isDeclared = false THEN 1 ELSE 0 END) as normalCompleted, " +
           "SUM(CASE WHEN i.isDeclared = true THEN 1 ELSE 0 END) as declared, " +
           "SUM(CASE WHEN i.isCompleted = true AND i.totalWickets = 10 THEN 1 ELSE 0 END) as allOut " +
           "FROM InningsEntity i")
    Object[] countInningsByResultType();

    /**
     * Find innings by bowler performance.
     */
    @Query("SELECT DISTINCT i FROM InningsEntity i " +
           "JOIN i.balls b ON b.bowlerName = :bowlerName " +
           "WHERE b.isWicket = true")
    List<InningsEntity> findInningsWithWicketsByBowler(@Param("bowlerName") String bowlerName);

    /**
     * Find innings by batsman performance.
     */
    @Query("SELECT DISTINCT i FROM InningsEntity i " +
           "JOIN i.balls b ON b.batsmanName = :batsmanName " +
           "WHERE b.runsScored > 0")
    List<InningsEntity> findInningsWithRunsByBatsman(@Param("batsmanName") String batsmanName);

    /**
     * Get partnership statistics for innings.
     */
    @Query("SELECT i, " +
           "SUM(b.runsScored + b.extraRuns) as partnershipRuns, " +
           "COUNT(b) as ballsPlayed " +
           "FROM InningsEntity i JOIN i.balls b " +
           "WHERE i.id = :inningsId " +
           "GROUP BY i")
    Object[] getInningsPartnershipStats(@Param("inningsId") Long inningsId);
}
