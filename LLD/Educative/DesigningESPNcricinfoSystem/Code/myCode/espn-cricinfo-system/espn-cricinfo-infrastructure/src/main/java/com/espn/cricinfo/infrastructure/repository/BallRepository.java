package com.espn.cricinfo.infrastructure.repository;

import com.espn.cricinfo.domain.enums.BallType;
import com.espn.cricinfo.infrastructure.entity.BallEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Ball entities.
 * Provides comprehensive data access for ball-by-ball analytics.
 */
@Repository
public interface BallRepository extends JpaRepository<BallEntity, Long> {

    /**
     * Find balls by match.
     */
    List<BallEntity> findByMatchId(Long matchId);

    /**
     * Find balls by innings.
     */
    List<BallEntity> findByInningsId(Long inningsId);

    /**
     * Find balls by over number.
     */
    List<BallEntity> findByOverNumber(Integer overNumber);

    /**
     * Find balls by bowler.
     */
    List<BallEntity> findByBowlerName(String bowlerName);

    /**
     * Find balls by batsman.
     */
    List<BallEntity> findByBatsmanName(String batsmanName);

    /**
     * Find wicket balls.
     */
    List<BallEntity> findByIsWicketTrue();

    /**
     * Find boundary balls.
     */
    List<BallEntity> findByIsBoundaryTrue();

    /**
     * Find six balls.
     */
    List<BallEntity> findByIsSixTrue();

    /**
     * Find balls by type.
     */
    List<BallEntity> findByBallType(BallType ballType);

    /**
     * Find balls with reviews.
     */
    List<BallEntity> findByReviewedTrue();

    /**
     * Find balls in time range.
     */
    List<BallEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Get bowler statistics for match.
     */
    @Query("SELECT b.bowlerName, " +
           "COUNT(b) as ballsBowled, " +
           "SUM(b.runsScored + b.extraRuns) as runsConceded, " +
           "SUM(CASE WHEN b.isWicket = true THEN 1 ELSE 0 END) as wickets " +
           "FROM BallEntity b WHERE b.match.id = :matchId " +
           "GROUP BY b.bowlerName")
    List<Object[]> getBowlerStatsForMatch(@Param("matchId") Long matchId);

    /**
     * Get batsman statistics for match.
     */
    @Query("SELECT b.batsmanName, " +
           "COUNT(b) as ballsFaced, " +
           "SUM(b.runsScored) as runsScored, " +
           "SUM(CASE WHEN b.isBoundary = true THEN 1 ELSE 0 END) as boundaries, " +
           "SUM(CASE WHEN b.isSix = true THEN 1 ELSE 0 END) as sixes " +
           "FROM BallEntity b WHERE b.match.id = :matchId " +
           "GROUP BY b.batsmanName")
    List<Object[]> getBatsmanStatsForMatch(@Param("matchId") Long matchId);

    /**
     * Get over-by-over analysis.
     */
    @Query("SELECT b.overNumber, " +
           "COUNT(b) as ballsInOver, " +
           "SUM(b.runsScored + b.extraRuns) as runsInOver, " +
           "SUM(CASE WHEN b.isWicket = true THEN 1 ELSE 0 END) as wicketsInOver " +
           "FROM BallEntity b WHERE b.match.id = :matchId " +
           "GROUP BY b.overNumber ORDER BY b.overNumber")
    List<Object[]> getOverByOverAnalysis(@Param("matchId") Long matchId);

    /**
     * Find dot balls in match.
     */
    @Query("SELECT b FROM BallEntity b WHERE b.match.id = :matchId AND b.runsScored = 0 AND b.isWicket = false")
    List<BallEntity> findDotBallsInMatch(@Param("matchId") Long matchId);

    /**
     * Get run rate analysis.
     */
    @Query("SELECT b.overNumber, " +
           "AVG(b.runsScored + b.extraRuns) as avgRunsPerBall, " +
           "COUNT(b) as ballCount " +
           "FROM BallEntity b WHERE b.match.id = :matchId " +
           "GROUP BY b.overNumber ORDER BY b.overNumber")
    List<Object[]> getRunRateAnalysis(@Param("matchId") Long matchId);

    /**
     * Find expensive overs (high scoring).
     */
    @Query("SELECT b.overNumber, SUM(b.runsScored + b.extraRuns) as runs " +
           "FROM BallEntity b WHERE b.match.id = :matchId " +
           "GROUP BY b.overNumber HAVING SUM(b.runsScored + b.extraRuns) >= :minRuns " +
           "ORDER BY SUM(b.runsScored + b.extraRuns) DESC")
    List<Object[]> findExpensiveOvers(@Param("matchId") Long matchId, @Param("minRuns") Integer minRuns);

    /**
     * Find maidens bowled.
     */
    @Query("SELECT b.bowlerName, b.overNumber " +
           "FROM BallEntity b WHERE b.match.id = :matchId " +
           "GROUP BY b.bowlerName, b.overNumber " +
           "HAVING SUM(b.runsScored + b.extraRuns) = 0 AND SUM(CASE WHEN b.isWicket = true THEN 1 ELSE 0 END) = 0")
    List<Object[]> findMaidens(@Param("matchId") Long matchId);

    /**
     * Get ball speed statistics.
     */
    @Query("SELECT AVG(b.ballSpeedKmh) as avgSpeed, " +
           "MAX(b.ballSpeedKmh) as maxSpeed, " +
           "MIN(b.ballSpeedKmh) as minSpeed, " +
           "COUNT(b) as totalBalls " +
           "FROM BallEntity b WHERE b.ballSpeedKmh IS NOT NULL")
    Object[] getBallSpeedStatistics();

    /**
     * Find balls with speed above threshold.
     */
    @Query("SELECT b FROM BallEntity b WHERE b.ballSpeedKmh >= :minSpeed ORDER BY b.ballSpeedKmh DESC")
    List<BallEntity> findFastBalls(@Param("minSpeed") Double minSpeed, Pageable pageable);

    /**
     * Get DRS statistics.
     */
    @Query("SELECT " +
           "COUNT(b) as totalReviews, " +
           "SUM(CASE WHEN b.reviewResult = 'OVERRULED' THEN 1 ELSE 0 END) as successfulReviews, " +
           "SUM(CASE WHEN b.reviewResult = 'UMPIRE_CALL' THEN 1 ELSE 0 END) as umpireCalls " +
           "FROM BallEntity b WHERE b.reviewed = true")
    Object[] getDRSStatistics();

    /**
     * Find controversial decisions.
     */
    @Query("SELECT b FROM BallEntity b WHERE b.reviewed = true AND " +
           "(b.umpireDecision != b.reviewResult OR b.reviewResult = 'OVERRULED')")
    List<BallEntity> findControversialDecisions();

    /**
     * Get partnership analysis.
     */
    @Query("SELECT b.batsmanName, b.bowlerName, " +
           "COUNT(b) as ballsFaced, " +
           "SUM(b.runsScored) as runsScored " +
           "FROM BallEntity b WHERE b.match.id = :matchId " +
           "GROUP BY b.batsmanName, b.bowlerName " +
           "ORDER BY SUM(b.runsScored) DESC")
    List<Object[]> getBatsmanVsBowlerStats(@Param("matchId") Long matchId);

    /**
     * Find balls with commentary.
     */
    List<BallEntity> findByCommentaryIsNotNull();

    /**
     * Get ball distribution by type.
     */
    @Query("SELECT b.ballType, COUNT(b) FROM BallEntity b GROUP BY b.ballType")
    List<Object[]> getBallTypeDistribution();

    /**
     * Find balls in specific over range.
     */
    @Query("SELECT b FROM BallEntity b WHERE b.overNumber BETWEEN :startOver AND :endOver")
    List<BallEntity> findBallsInOverRange(@Param("startOver") Integer startOver,
                                         @Param("endOver") Integer endOver);

    /**
     * Get real-time match updates (recent balls).
     */
    @Query("SELECT b FROM BallEntity b WHERE b.match.id = :matchId AND b.timestamp >= :since " +
           "ORDER BY b.timestamp DESC")
    List<BallEntity> getRecentBalls(@Param("matchId") Long matchId,
                                   @Param("since") LocalDateTime since);
}
