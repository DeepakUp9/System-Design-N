package com.espn.cricinfo.core.strategy;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Match;
import com.espn.cricinfo.domain.enums.MatchFormat;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Context class for the Match Format Strategy Pattern.
 * Manages the current strategy and delegates operations to the appropriate strategy implementation.
 *
 * This class provides a unified interface for different match format rules while keeping
 * the core logic decoupled from specific format implementations.
 */
@Slf4j
public class MatchFormatContext {

    private MatchFormatStrategy strategy;

    /**
     * Sets the match format strategy.
     *
     * @param strategy the strategy to use
     */
    public void setStrategy(MatchFormatStrategy strategy) {
        this.strategy = strategy;
        log.info("Match format strategy changed to: {}", strategy.getFormatName());
    }

    /**
     * Sets the strategy based on match format enum.
     *
     * @param format the match format
     */
    public void setStrategy(MatchFormat format) {
        MatchFormatStrategy newStrategy = switch (format) {
            case T20 -> new T20MatchStrategy();
            case ODI -> new ODIMatchStrategy();
            case TEST -> new TestMatchStrategy();
            case T10 -> new T20MatchStrategy(); // T10 uses similar rules to T20
            case HUNDRED -> new ODIMatchStrategy(); // The Hundred uses similar rules to ODI
        };

        setStrategy(newStrategy);
    }

    /**
     * Validates and processes a ball according to the current strategy.
     *
     * @param ball the ball to process
     * @return true if the ball is valid and processed successfully
     * @throws IllegalStateException if no strategy is set
     */
    public boolean processBall(Ball ball) {
        if (strategy == null) {
            throw new IllegalStateException("No match format strategy is set");
        }

        boolean isValid = strategy.validateBall(ball);
        if (!isValid) {
            log.warn("Ball validation failed for format {}: {}", strategy.getFormatName(), ball);
            return false;
        }

        log.debug("Ball processed successfully for format {}: {}", strategy.getFormatName(), ball.getBallDescription());
        return true;
    }

    /**
     * Calculates the score for a ball using the current strategy.
     *
     * @param ball the ball to score
     * @return the scoring result
     * @throws IllegalStateException if no strategy is set
     */
    public ScoringResult calculateScore(Ball ball) {
        if (strategy == null) {
            throw new IllegalStateException("No match format strategy is set");
        }

        return strategy.calculateScore(ball);
    }

    /**
     * Checks if the match is complete according to current strategy rules.
     *
     * @param match the match to check
     * @return true if the match is complete
     * @throws IllegalStateException if no strategy is set
     */
    public boolean isMatchComplete(Match match) {
        if (strategy == null) {
            throw new IllegalStateException("No match format strategy is set");
        }

        return strategy.isMatchComplete(match);
    }

    /**
     * Gets the maximum overs per innings for the current strategy.
     *
     * @return maximum overs per innings (0 for unlimited)
     * @throws IllegalStateException if no strategy is set
     */
    public int getMaxOversPerInnings() {
        if (strategy == null) {
            throw new IllegalStateException("No match format strategy is set");
        }

        return strategy.getMaxOversPerInnings();
    }

    /**
     * Gets the maximum innings for the current strategy.
     *
     * @return maximum innings
     * @throws IllegalStateException if no strategy is set
     */
    public int getMaxInnings() {
        if (strategy == null) {
            throw new IllegalStateException("No match format strategy is set");
        }

        return strategy.getMaxInnings();
    }

    /**
     * Checks if innings declaration is allowed in the current strategy.
     *
     * @return true if declaration is allowed
     * @throws IllegalStateException if no strategy is set
     */
    public boolean canDeclareInnings() {
        if (strategy == null) {
            throw new IllegalStateException("No match format strategy is set");
        }

        return strategy.canDeclareInnings();
    }

    /**
     * Validates the current match state.
     *
     * @param match the match to validate
     * @return true if the match state is valid
     * @throws IllegalStateException if no strategy is set
     */
    public boolean validateMatchState(Match match) {
        if (strategy == null) {
            throw new IllegalStateException("No match format strategy is set");
        }

        return strategy.validateMatchState(match);
    }

    /**
     * Gets the current strategy name.
     *
     * @return strategy name or null if no strategy is set
     */
    public String getCurrentStrategyName() {
        return strategy != null ? strategy.getFormatName() : null;
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
