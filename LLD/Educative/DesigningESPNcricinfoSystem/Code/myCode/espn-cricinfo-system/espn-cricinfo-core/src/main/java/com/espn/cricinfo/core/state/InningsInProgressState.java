package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Innings;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * State representing an innings that is currently in progress.
 * Handles ball processing, wickets, and innings completion.
 */
@Slf4j
public class InningsInProgressState implements InningsState {

    @Override
    public String getStateName() {
        return "IN_PROGRESS";
    }

    @Override
    public boolean canStartInnings(Innings innings) {
        return false; // Already started
    }

    @Override
    public void startInnings(Innings innings) {
        throw new IllegalStateException("Innings is already in progress");
    }

    @Override
    public boolean canProcessBall(Innings innings, Ball ball) {
        // Can process balls if innings is active and not all out
        return innings != null &&
               !innings.isCompleted() &&
               !innings.isAllOut() &&
               ball != null;
    }

    @Override
    public void processBall(Innings innings, Ball ball) {
        if (!canProcessBall(innings, ball)) {
            throw new IllegalStateException("Cannot process ball: innings not active or invalid ball");
        }

        log.debug("Processing ball in innings {}: {}", innings.getInningsNumber(), ball.getBallDescription());

        // Update innings statistics
        innings.setTotalRuns(innings.getTotalRuns() + ball.getTotalRuns());
        innings.setTotalBalls(innings.getTotalBalls() + 1);

        // Handle over completion
        if (ball.isOverComplete()) {
            innings.setTotalOvers(innings.getTotalOvers() + 1);
        }

        // Handle wickets
        if (ball.isWicket()) {
            innings.setTotalWickets(innings.getTotalWickets() + 1);
            handleWicket(innings, "CAUGHT"); // Default wicket type
        }

        // Add ball to innings record
        if (innings.getBalls() == null) {
            innings.setBalls(new ArrayList<>());
        }
        innings.getBalls().add(ball);

        // Check if innings should be completed
        if (shouldCompleteInnings(innings)) {
            completeInnings(innings);
        }
    }

    @Override
    public boolean canCompleteInnings(Innings innings) {
        return innings != null &&
               !innings.isCompleted() &&
               (innings.isAllOut() || innings.isDeclared());
    }

    @Override
    public void completeInnings(Innings innings) {
        if (!canCompleteInnings(innings)) {
            throw new IllegalStateException("Cannot complete innings: not eligible for completion");
        }

        log.info("Completing innings {}: {} runs, {} wickets",
                innings.getInningsNumber(), innings.getTotalRuns(), innings.getTotalWickets());

        innings.setCompleted(true);
    }

    @Override
    public boolean canDeclareInnings(Innings innings) {
        // Declaration is only allowed in Test cricket and innings must be in progress
        return innings != null &&
               !innings.isCompleted() &&
               !innings.isAllOut() &&
               innings.getTotalBalls() >= 60; // Minimum 10 overs
    }

    @Override
    public void declareInnings(Innings innings) {
        if (!canDeclareInnings(innings)) {
            throw new IllegalStateException("Cannot declare innings: not eligible for declaration");
        }

        log.info("Declaring innings {}: {} runs, {} wickets",
                innings.getInningsNumber(), innings.getTotalRuns(), innings.getTotalWickets());

        innings.setDeclared(true);
        innings.setCompleted(true);
    }

    @Override
    public boolean canTakeWicket(Innings innings) {
        return innings != null &&
               !innings.isCompleted() &&
               !innings.isAllOut();
    }

    @Override
    public void handleWicket(Innings innings, String wicketType) {
        if (!canTakeWicket(innings)) {
            throw new IllegalStateException("Cannot take wicket: innings not active");
        }

        log.info("Wicket in innings {}: {} ({} wickets total)",
                innings.getInningsNumber(), wicketType, innings.getTotalWickets());

        // Additional wicket handling logic can be added here
        // e.g., updating player statistics, checking for innings completion
    }

    /**
     * Determines if the innings should be completed based on current state.
     */
    private boolean shouldCompleteInnings(Innings innings) {
        // Innings completes if team is all out
        if (innings.isAllOut()) {
            log.info("Innings {} completed: team all out", innings.getInningsNumber());
            return true;
        }

        // Innings completes if declared
        if (innings.isDeclared()) {
            log.info("Innings {} completed: declared", innings.getInningsNumber());
            return true;
        }

        // Additional completion conditions can be added here
        // e.g., target reached in chase, rain interruption, etc.

        return false;
    }
}
