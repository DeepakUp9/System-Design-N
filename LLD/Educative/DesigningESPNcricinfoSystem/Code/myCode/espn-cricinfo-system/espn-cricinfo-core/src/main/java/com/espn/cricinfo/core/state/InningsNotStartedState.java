package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Innings;
import lombok.extern.slf4j.Slf4j;

/**
 * State representing an innings that has not started yet.
 * This is the initial state of all innings.
 */
@Slf4j
public class InningsNotStartedState implements InningsState {

    @Override
    public String getStateName() {
        return "NOT_STARTED";
    }

    @Override
    public boolean canStartInnings(Innings innings) {
        // Can start if teams are assigned and basic setup is complete
        return innings != null &&
               innings.getBattingTeam() != null &&
               innings.getBowlingTeam() != null &&
               innings.getInningsNumber() > 0;
    }

    @Override
    public void startInnings(Innings innings) {
        if (!canStartInnings(innings)) {
            throw new IllegalStateException("Cannot start innings: missing required information");
        }

        log.info("Starting innings {}: {} vs {}",
                innings.getInningsNumber(),
                innings.getBattingTeam().getName(),
                innings.getBowlingTeam().getName());

        // Initialize innings statistics
        innings.setTotalRuns(0);
        innings.setTotalWickets(0);
        innings.setTotalOvers(0);
        innings.setTotalBalls(0);
        innings.setCompleted(false);
        innings.setDeclared(false);
    }

    @Override
    public boolean canProcessBall(Innings innings, Ball ball) {
        return false; // Cannot process balls before innings starts
    }

    @Override
    public void processBall(Innings innings, Ball ball) {
        throw new IllegalStateException("Cannot process ball: innings not started");
    }

    @Override
    public boolean canCompleteInnings(Innings innings) {
        return false; // Cannot complete innings that hasn't started
    }

    @Override
    public void completeInnings(Innings innings) {
        throw new IllegalStateException("Cannot complete innings: innings not started");
    }

    @Override
    public boolean canDeclareInnings(Innings innings) {
        return false; // Cannot declare innings that hasn't started
    }

    @Override
    public void declareInnings(Innings innings) {
        throw new IllegalStateException("Cannot declare innings: innings not started");
    }

    @Override
    public boolean canTakeWicket(Innings innings) {
        return false; // Cannot take wickets before innings starts
    }

    @Override
    public void handleWicket(Innings innings, String wicketType) {
        throw new IllegalStateException("Cannot handle wicket: innings not started");
    }
}
