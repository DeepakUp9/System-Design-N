package com.espn.cricinfo.core.strategy;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Match;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;

/**
 * Strategy interface for different cricket match formats.
 * Defines the rules and constraints for various formats like T20, ODI, Test.
 *
 * This is part of the Strategy Pattern implementation for handling
 * different match format rules in a decoupled manner.
 */
public interface MatchFormatStrategy {

    /**
     * Determines if a match is complete based on format-specific rules.
     *
     * @param match the match to evaluate
     * @return true if the match is complete, false otherwise
     */
    boolean isMatchComplete(Match match);

    /**
     * Gets the maximum number of overs allowed per innings for this format.
     * Returns 0 for Test cricket (unlimited).
     *
     * @return maximum overs per innings
     */
    int getMaxOversPerInnings();

    /**
     * Gets the maximum number of innings allowed in this format.
     *
     * @return maximum innings
     */
    int getMaxInnings();

    /**
     * Checks if innings can be declared in this format.
     * Only applicable for Test cricket.
     *
     * @return true if declaration is allowed
     */
    boolean canDeclareInnings();

    /**
     * Validates if a ball is legal according to format rules.
     *
     * @param ball the ball to validate
     * @return true if the ball is valid for this format
     */
    boolean validateBall(Ball ball);

    /**
     * Calculates the scoring result for a ball based on format rules.
     *
     * @param ball the ball to score
     * @return the scoring result
     */
    ScoringResult calculateScore(Ball ball);

    /**
     * Gets the format name for identification.
     *
     * @return format name
     */
    String getFormatName();

    /**
     * Validates match state transitions.
     *
     * @param match the match to validate
     * @return true if the current state is valid
     */
    boolean validateMatchState(Match match);
}
