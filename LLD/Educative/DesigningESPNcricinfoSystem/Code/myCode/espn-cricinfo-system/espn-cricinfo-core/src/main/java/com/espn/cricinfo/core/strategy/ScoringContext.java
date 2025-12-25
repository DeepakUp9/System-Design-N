package com.espn.cricinfo.core.strategy;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.valueobjects.PlayerStats;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Context class for the Scoring Strategy Pattern.
 * Manages the current scoring strategy and delegates operations to the appropriate implementation.
 */
@Slf4j
public class ScoringContext {

    private ScoringStrategy strategy;

    /**
     * Sets the scoring strategy.
     *
     * @param strategy the strategy to use
     */
    public void setStrategy(ScoringStrategy strategy) {
        this.strategy = strategy;
        log.info("Scoring strategy changed to: {}", strategy.getStrategyName());
    }

    /**
     * Sets the default standard scoring strategy.
     */
    public void setStandardStrategy() {
        setStrategy(new StandardScoringStrategy());
    }

    /**
     * Calculates the score for a ball using the current strategy.
     *
     * @param ball the ball to score
     * @return the scoring result
     * @throws IllegalStateException if no strategy is set
     */
    public ScoringResult calculateBallScore(Ball ball) {
        if (strategy == null) {
            throw new IllegalStateException("No scoring strategy is set");
        }

        ScoringResult result = strategy.calculateBallScore(ball);
        log.debug("Ball scored using {}: {}", strategy.getStrategyName(), result.getDescription());
        return result;
    }

    /**
     * Validates if the given score is valid.
     *
     * @param runs the runs scored
     * @param isExtra whether it's an extra
     * @return true if valid
     * @throws IllegalStateException if no strategy is set
     */
    public boolean isValidScore(int runs, boolean isExtra) {
        if (strategy == null) {
            throw new IllegalStateException("No scoring strategy is set");
        }

        return strategy.isValidScore(runs, isExtra);
    }

    /**
     * Gets the maximum runs per ball for the current strategy.
     *
     * @return maximum runs per ball
     * @throws IllegalStateException if no strategy is set
     */
    public int getMaxRunsPerBall() {
        if (strategy == null) {
            throw new IllegalStateException("No scoring strategy is set");
        }

        return strategy.getMaxRunsPerBall();
    }

    /**
     * Calculates bonus points for player performance.
     *
     * @param stats the player's statistics
     * @return bonus points
     * @throws IllegalStateException if no strategy is set
     */
    public int calculateBonusPoints(PlayerStats stats) {
        if (strategy == null) {
            throw new IllegalStateException("No scoring strategy is set");
        }

        return strategy.calculateBonusPoints(stats);
    }

    /**
     * Updates player statistics based on a ball.
     *
     * @param currentStats current player statistics
     * @param ball the ball played
     * @return updated statistics
     * @throws IllegalStateException if no strategy is set
     */
    public PlayerStats updatePlayerStats(PlayerStats currentStats, Ball ball) {
        if (strategy == null) {
            throw new IllegalStateException("No scoring strategy is set");
        }

        return strategy.updatePlayerStats(currentStats, ball);
    }

    /**
     * Gets the current strategy name.
     *
     * @return strategy name or null if no strategy is set
     */
    public String getCurrentStrategyName() {
        return strategy != null ? strategy.getStrategyName() : null;
    }

    /**
     * Checks if a strategy is currently set.
     *
     * @return true if strategy is set
     */
    public boolean hasStrategy() {
        return strategy != null;
    }
}
