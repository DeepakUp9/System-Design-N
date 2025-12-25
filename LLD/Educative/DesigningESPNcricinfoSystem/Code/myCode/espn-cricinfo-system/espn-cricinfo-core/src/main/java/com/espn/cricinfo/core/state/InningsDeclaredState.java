package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Innings;

/**
 * State representing an innings that has been declared (Test cricket only).
 * This is a terminal state similar to completed, but specifically for declarations.
 */
public class InningsDeclaredState implements InningsState {

    @Override
    public String getStateName() {
        return "DECLARED";
    }

    @Override
    public boolean canStartInnings(Innings innings) {
        return false; // Already declared (and completed)
    }

    @Override
    public void startInnings(Innings innings) {
        throw new IllegalStateException("Cannot start a declared innings");
    }

    @Override
    public boolean canProcessBall(Innings innings, Ball ball) {
        return false; // Cannot process balls in declared innings
    }

    @Override
    public void processBall(Innings innings, Ball ball) {
        throw new IllegalStateException("Cannot process ball: innings declared");
    }

    @Override
    public boolean canCompleteInnings(Innings innings) {
        return false; // Already completed when declared
    }

    @Override
    public void completeInnings(Innings innings) {
        throw new IllegalStateException("Innings is already declared and completed");
    }

    @Override
    public boolean canDeclareInnings(Innings innings) {
        return false; // Already declared
    }

    @Override
    public void declareInnings(Innings innings) {
        throw new IllegalStateException("Innings is already declared");
    }

    @Override
    public boolean canTakeWicket(Innings innings) {
        return false; // Cannot take wickets in declared innings
    }

    @Override
    public void handleWicket(Innings innings, String wicketType) {
        throw new IllegalStateException("Cannot handle wicket: innings declared");
    }
}
