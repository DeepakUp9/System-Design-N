package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Match;
import lombok.extern.slf4j.Slf4j;

/**
 * State representing a match that is currently paused.
 * Can be resumed or abandoned from this state.
 */
@Slf4j
public class MatchPausedState implements MatchState {

    @Override
    public String getStateName() {
        return "PAUSED";
    }

    @Override
    public boolean canStartMatch(Match match) {
        return false; // Match was already started
    }

    @Override
    public void startMatch(Match match) {
        throw new IllegalStateException("Cannot start an already paused match");
    }

    @Override
    public boolean canPauseMatch(Match match) {
        return false; // Already paused
    }

    @Override
    public void pauseMatch(Match match) {
        throw new IllegalStateException("Match is already paused");
    }

    @Override
    public boolean canResumeMatch(Match match) {
        return true; // Can always resume a paused match
    }

    @Override
    public void resumeMatch(Match match) {
        log.info("Resuming paused match: {}", match.getMatchName());
        match.setStatus(Match.MatchStatus.IN_PROGRESS);
    }

    @Override
    public boolean canEndMatch(Match match) {
        return false; // Cannot end a paused match directly
    }

    @Override
    public void endMatch(Match match) {
        throw new IllegalStateException("Cannot end a paused match - resume first or abandon");
    }

    @Override
    public boolean canAbandonMatch(Match match) {
        return true; // Can abandon a paused match
    }

    @Override
    public void abandonMatch(Match match) {
        log.warn("Abandoning paused match: {}", match.getMatchName());
        match.setStatus(Match.MatchStatus.ABANDONED);
        // Note: endTime should be set when abandoning
    }

    @Override
    public boolean canStartNewInnings(Match match) {
        return false; // Cannot start new innings while paused
    }

    @Override
    public void handleInningsCompletion(Match match) {
        log.debug("Ignoring innings completion in PAUSED state - match must be resumed first");
    }
}
