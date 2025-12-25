package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Match;

/**
 * State representing a match that has been abandoned.
 * This is a terminal state - no further transitions allowed.
 * Abandoned matches may be due to weather, safety concerns, etc.
 */
public class MatchAbandonedState implements MatchState {

    @Override
    public String getStateName() {
        return "ABANDONED";
    }

    @Override
    public boolean canStartMatch(Match match) {
        return false; // Match is already abandoned
    }

    @Override
    public void startMatch(Match match) {
        throw new IllegalStateException("Cannot start an abandoned match");
    }

    @Override
    public boolean canPauseMatch(Match match) {
        return false; // Cannot pause an abandoned match
    }

    @Override
    public void pauseMatch(Match match) {
        throw new IllegalStateException("Cannot pause an abandoned match");
    }

    @Override
    public boolean canResumeMatch(Match match) {
        return false; // Cannot resume an abandoned match
    }

    @Override
    public void resumeMatch(Match match) {
        throw new IllegalStateException("Cannot resume an abandoned match");
    }

    @Override
    public boolean canEndMatch(Match match) {
        return false; // Already abandoned (terminal state)
    }

    @Override
    public void endMatch(Match match) {
        throw new IllegalStateException("Match is already abandoned");
    }

    @Override
    public boolean canAbandonMatch(Match match) {
        return false; // Already abandoned
    }

    @Override
    public void abandonMatch(Match match) {
        throw new IllegalStateException("Match is already abandoned");
    }

    @Override
    public boolean canStartNewInnings(Match match) {
        return false; // Cannot start innings in abandoned match
    }

    @Override
    public void handleInningsCompletion(Match match) {
        // Nothing to do - match is already abandoned
    }
}
