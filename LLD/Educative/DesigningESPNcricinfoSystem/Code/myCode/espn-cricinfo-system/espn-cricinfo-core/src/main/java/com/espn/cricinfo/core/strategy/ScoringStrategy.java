package com.espn.cricinfo.core.strategy;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.valueobjects.PlayerStats;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;

/**
 * Strategy interface for different scoring calculation methods.
 * Defines how runs, wickets, and other statistics are calculated.
 *
 * This allows for different scoring algorithms based on format, era, or custom rules.
 */
public interface ScoringStrategy {

    /**
     * Calculates the scoring result for a ball.
     *
     * @param ball the ball to score
     * @return the scoring result
     */
    ScoringResult calculateBallScore(Ball ball);

    /**
     * Validates if the given runs are valid for a ball.
     *
     * @param runs the runs scored
     * @param isExtra whether it's an extra
     * @return true if valid
     */
    boolean isValidScore(int runs, boolean isExtra);

    /**
     * Gets the maximum runs that can be scored on a single ball.
     *
     * @return maximum runs per ball
     */
    int getMaxRunsPerBall();

    /**
     * Calculates bonus points for player performance.
     *
     * @param stats the player's statistics
     * @return bonus points value
     */
    int calculateBonusPoints(PlayerStats stats);

    /**
     * Updates player statistics based on a ball.
     *
     * @param currentStats current player statistics
     * @param ball the ball played
     * @return updated statistics
     */
    PlayerStats updatePlayerStats(PlayerStats currentStats, Ball ball);

    /**
     * Gets the strategy name for identification.
     *
     * @return strategy name
     */
    String getStrategyName();
}
