package com.espn.cricinfo.core.service;

import com.espn.cricinfo.core.state.InningsContext;
import com.espn.cricinfo.core.state.MatchContext;
import com.espn.cricinfo.core.state.PlayerContext;
import com.espn.cricinfo.core.strategy.MatchFormatContext;
import com.espn.cricinfo.core.strategy.ScoringContext;
import com.espn.cricinfo.domain.entities.*;
import com.espn.cricinfo.domain.enums.BallType;
import com.espn.cricinfo.domain.enums.MatchFormat;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Core business service for match scoring operations.
 * Orchestrates Strategy and State patterns for cricket scoring logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MatchScoringService {

    private final MatchFormatContext matchFormatContext;
    private final ScoringContext scoringContext;

    /**
     * Initialize match scoring with appropriate strategies.
     */
    public void initializeMatchScoring(Match match) {
        log.info("Initializing scoring for match: {}", match.getMatchName());

        // Set match format strategy
        matchFormatContext.setStrategy(match.getFormat());

        // Set standard scoring strategy
        scoringContext.setStandardStrategy();

        // Validate match state
        if (!matchFormatContext.validateMatchState(match)) {
            throw new IllegalStateException("Invalid match state for scoring");
        }

        log.info("Match scoring initialized with format: {}", match.getFormat());
    }

    /**
     * Process a ball delivery and update all related state.
     */
    public ScoringResult processBallDelivery(MatchContext matchContext,
                                           InningsContext inningsContext,
                                           Ball ball) {
        log.debug("Processing ball delivery: {}", ball.getBallDescription());

        // Validate ball against match format rules
        boolean isValidBall = matchFormatContext.processBall(ball);
        if (!isValidBall) {
            throw new IllegalArgumentException("Invalid ball for current match format");
        }

        // Calculate scoring result
        ScoringResult scoringResult = matchFormatContext.calculateScore(ball);

        // Update innings with the ball
        inningsContext.processBall(ball);

        // Update player statistics if wicket or runs scored
        updatePlayerStatistics(ball, scoringResult);

        // Check if innings should transition (all out, target reached, etc.)
        checkInningsTransitions(matchContext, inningsContext);

        log.info("Ball processed successfully: {} - {}", ball.getBallDescription(),
                scoringResult.getDescription());

        return scoringResult;
    }

    /**
     * Handle innings completion and transitions.
     */
    public void handleInningsCompletion(MatchContext matchContext,
                                      InningsContext inningsContext) {
        log.info("Handling innings completion for innings: {}", inningsContext.getInnings().getInningsNumber());

        // Complete the innings
        inningsContext.completeInnings();

        // Check if match should end or new innings should start
        if (shouldStartNewInnings(matchContext)) {
            matchContext.handleInningsCompletion();
        } else if (shouldEndMatch(matchContext)) {
            matchContext.endMatch();
        }
    }

    /**
     * Handle innings declaration (Test cricket).
     */
    public void declareInnings(InningsContext inningsContext, double overs, int balls) {
        log.info("Declaring innings {} at {} overs and {} balls",
                inningsContext.getInnings().getInningsNumber(), overs, balls);

        inningsContext.declareInnings(overs, balls);

        // Declaration effectively completes the innings
        inningsContext.completeInnings();
    }

    /**
     * Process wicket event for a player.
     */
    public void processWicket(PlayerContext playerContext, String wicketType) {
        log.info("Processing wicket for player {}: {}", playerContext.getPlayerName(), wicketType);

        playerContext.getOut(wicketType);

        // Additional wicket processing logic can be added here
        // e.g., update bowling statistics, check for milestones
    }

    /**
     * Validate if a ball can be processed in current state.
     */
    public boolean validateBallForCurrentState(MatchContext matchContext,
                                             InningsContext inningsContext,
                                             Ball ball) {
        // Check match state allows ball processing
        if (!matchContext.canStartNewInnings() && !matchContext.isActive()) {
            return false;
        }

        // Check innings state allows ball processing
        if (!inningsContext.isActive()) {
            return false;
        }

        // Validate ball against format rules
        return matchFormatContext.validateBall(ball);
    }

    /**
     * Get current match statistics.
     */
    public MatchStatistics getMatchStatistics(Match match) {
        return MatchStatistics.builder()
                .totalRuns(match.getTotalRuns())
                .totalWickets(calculateTotalWickets(match))
                .totalOvers(match.getTotalOvers())
                .currentRunRate(calculateCurrentRunRate(match))
                .build();
    }

    /**
     * Get current innings statistics.
     */
    public InningsStatistics getInningsStatistics(Innings innings) {
        return InningsStatistics.builder()
                .totalRuns(innings.getTotalRuns())
                .totalWickets(innings.getTotalWickets())
                .totalOvers(innings.getTotalOvers())
                .runRate(innings.getRunRate())
                .isAllOut(innings.isAllOut())
                .isCompleted(innings.isCompleted())
                .isDeclared(innings.isDeclared())
                .build();
    }

    private void updatePlayerStatistics(Ball ball, ScoringResult scoringResult) {
        // This would integrate with player statistics service
        // For now, just log the scoring event
        if (scoringResult.isWicket()) {
            log.info("Wicket recorded for batsman: {}", ball.getBatsmanName());
        }

        if (scoringResult.getRunsScored() > 0) {
            log.debug("Runs scored by {}: {}", ball.getBatsmanName(), scoringResult.getRunsScored());
        }

        if (scoringResult.isBoundary() || scoringResult.isSix()) {
            log.info("Boundary/Six scored by {}: {} runs", ball.getBatsmanName(),
                    scoringResult.getRunsScored());
        }
    }

    private void checkInningsTransitions(MatchContext matchContext, InningsContext inningsContext) {
        Innings innings = inningsContext.getInnings();

        // Check for innings completion conditions
        if (innings.isAllOut() || innings.isDeclared()) {
            log.info("Innings {} completed - all out: {}, declared: {}",
                    innings.getInningsNumber(), innings.isAllOut(), innings.isDeclared());
            handleInningsCompletion(matchContext, inningsContext);
        }
    }

    private boolean shouldStartNewInnings(MatchContext matchContext) {
        return matchContext.canStartNewInnings();
    }

    private boolean shouldEndMatch(MatchContext matchContext) {
        return matchContext.getMatch().getCurrentInningsNumber() >=
               matchContext.getMatch().getMaxInnings();
    }

    private int calculateTotalWickets(Match match) {
        return match.getInnings().stream()
                .mapToInt(Innings::getTotalWickets)
                .sum();
    }

    private double calculateCurrentRunRate(Match match) {
        return match.getTotalOvers() > 0 ?
               (double) match.getTotalRuns() / match.getTotalOvers() : 0.0;
    }

    // DTOs for statistics
    public record MatchStatistics(
            int totalRuns,
            int totalWickets,
            int totalOvers,
            double currentRunRate
    ) {}

    public record InningsStatistics(
            int totalRuns,
            int totalWickets,
            int totalOvers,
            double runRate,
            boolean isAllOut,
            boolean isCompleted,
            boolean isDeclared
    ) {}
}
