package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Match;

/**
 * State representing a match that has been completed.
 * This is a terminal state - no further transitions allowed.
 */
public class MatchCompletedState implements MatchState {

    @Override
    public String getStateName() {
        return "COMPLETED";
    }

    @Override
    public boolean canStartMatch(Match match) {
        return false; // Match is already completed
    }

    @Override
    public void startMatch(Match match) {
        throw new IllegalStateException("Cannot start a completed match");
    }

    @Override
    public boolean canPauseMatch(Match match) {
        return false; // Cannot pause a completed match
    }

    @Override
    public void pauseMatch(Match match) {
        throw new IllegalStateException("Cannot pause a completed match");
    }

    @Override
    public boolean canResumeMatch(Match match) {
        return false; // Cannot resume a completed match
    }

    @Override
    public void resumeMatch(Match match) {
        throw new IllegalStateException("Cannot resume a completed match");
    }

    @Override
    public boolean canEndMatch(Match match) {
        return false; // Already completed
    }

    @Override
    public void endMatch(Match match) {
        throw new IllegalStateException("Match is already completed");
    }

    @Override
    public boolean canAbandonMatch(Match match) {
        return false; // Cannot abandon a completed match
    }

    @Override
    public void abandonMatch(Match match) {
        throw new IllegalStateException("Cannot abandon a completed match");
    }

    @Override
    public boolean canStartNewInnings(Match match) {
        return false; // Cannot start innings in completed match
    }

    @Override
    public void handleInningsCompletion(Match match) {
        // Nothing to do - match is already completed
    }
}
