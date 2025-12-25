package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Innings;

/**
 * State representing an innings that has been completed.
 * This is a terminal state - no further actions allowed.
 */
public class InningsCompletedState implements InningsState {

    @Override
    public String getStateName() {
        return "COMPLETED";
    }

    @Override
    public boolean canStartInnings(Innings innings) {
        return false; // Already completed
    }

    @Override
    public void startInnings(Innings innings) {
        throw new IllegalStateException("Cannot start a completed innings");
    }

    @Override
    public boolean canProcessBall(Innings innings, Ball ball) {
        return false; // Cannot process balls in completed innings
    }

    @Override
    public void processBall(Innings innings, Ball ball) {
        throw new IllegalStateException("Cannot process ball: innings completed");
    }

    @Override
    public boolean canCompleteInnings(Innings innings) {
        return false; // Already completed
    }

    @Override
    public void completeInnings(Innings innings) {
        throw new IllegalStateException("Innings is already completed");
    }

    @Override
    public boolean canDeclareInnings(Innings innings) {
        return false; // Cannot declare a completed innings
    }

    @Override
    public void declareInnings(Innings innings) {
        throw new IllegalStateException("Cannot declare a completed innings");
    }

    @Override
    public boolean canTakeWicket(Innings innings) {
        return false; // Cannot take wickets in completed innings
    }

    @Override
    public void handleWicket(Innings innings, String wicketType) {
        throw new IllegalStateException("Cannot handle wicket: innings completed");
    }
}
