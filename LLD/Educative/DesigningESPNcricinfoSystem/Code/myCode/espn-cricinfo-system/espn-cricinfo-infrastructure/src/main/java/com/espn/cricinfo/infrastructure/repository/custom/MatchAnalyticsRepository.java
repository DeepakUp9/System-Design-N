package com.espn.cricinfo.infrastructure.repository.custom;

import com.espn.cricinfo.domain.enums.MatchFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Custom repository interface for advanced match analytics.
 * Provides complex queries and aggregations not easily expressible with standard JPA.
 */
public interface MatchAnalyticsRepository {

    /**
     * Get comprehensive match statistics for a date range.
     */
    Map<String, Object> getMatchStatistics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get head-to-head statistics between two teams.
     */
    Map<String, Object> getHeadToHeadStatistics(Long team1Id, Long team2Id);

    /**
     * Get venue performance statistics.
     */
    Map<String, Object> getVenuePerformanceStats(Long venueId);

    /**
     * Get player performance in different conditions.
     */
    Map<String, Object> getPlayerPerformanceInConditions(Long playerId, String condition);

    /**
     * Get match prediction factors based on historical data.
     */
    Map<String, Object> getMatchPredictionFactors(Long team1Id, Long team2Id, Long venueId);

    /**
     * Get tournament progression analytics.
     */
    Map<String, Object> getTournamentProgression(Long tournamentId);

    /**
     * Get real-time match momentum analysis.
     */
    Map<String, Object> getMatchMomentumAnalysis(Long matchId);

    /**
     * Get comparative statistics across formats.
     */
    Map<String, Object> getFormatComparisonStats();

    /**
     * Get player career trajectory analysis.
     */
    Map<String, Object> getPlayerCareerTrajectory(Long playerId);

    /**
     * Get team performance trends.
     */
    List<Map<String, Object>> getTeamPerformanceTrends(Long teamId, int months);

    /**
     * Get match quality metrics (competitiveness, etc.).
     */
    Map<String, Object> getMatchQualityMetrics(Long matchId);

    /**
     * Get umpire decision accuracy statistics.
     */
    Map<String, Object> getUmpireDecisionStats(String umpireName);

    /**
     * Get weather impact analysis on matches.
     */
    Map<String, Object> getWeatherImpactAnalysis();

    /**
     * Get ball-by-ball pressure analysis.
     */
    List<Map<String, Object>> getBallByBallPressureAnalysis(Long matchId);

    /**
     * Get strategic decision impact analysis.
     */
    Map<String, Object> getStrategicDecisionImpact(Long matchId);

    /**
     * Get crowd influence analysis (if attendance data available).
     */
    Map<String, Object> getCrowdInfluenceAnalysis(Long venueId);
}
