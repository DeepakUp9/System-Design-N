package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Match;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * State representing a match that has not started yet.
 * This is the initial state of all matches.
 */
@Slf4j
public class MatchNotStartedState implements MatchState {

    @Override
    public String getStateName() {
        return "NOT_STARTED";
    }

    @Override
    public boolean canStartMatch(Match match) {
        // Can start if teams are set and basic validation passes
        return match != null &&
               match.getTeam1() != null &&
               match.getTeam2() != null &&
               match.getVenue() != null;
    }

    @Override
    public void startMatch(Match match) {
        if (!canStartMatch(match)) {
            throw new IllegalStateException("Cannot start match: missing required information");
        }

        log.info("Starting match: {}", match.getMatchName());
        match.setStatus(Match.MatchStatus.IN_PROGRESS);
        match.setStartTime(LocalDateTime.now());

        // Initialize first innings
        match.setCurrentInningsNumber(1);
        match.setIsFirstInnings(true);
        match.setIsSecondInnings(false);
    }

    @Override
    public boolean canPauseMatch(Match match) {
        return false; // Cannot pause a match that hasn't started
    }

    @Override
    public void pauseMatch(Match match) {
        throw new IllegalStateException("Cannot pause a match that hasn't started");
    }

    @Override
    public boolean canResumeMatch(Match match) {
        return false; // Cannot resume a match that hasn't started
    }

    @Override
    public void resumeMatch(Match match) {
        throw new IllegalStateException("Cannot resume a match that hasn't started");
    }

    @Override
    public boolean canEndMatch(Match match) {
        return true; // Can always cancel a match that hasn't started
    }

    @Override
    public void endMatch(Match match) {
        log.info("Cancelling match before start: {}", match.getMatchName());
        match.setStatus(Match.MatchStatus.COMPLETED);
        match.setEndTime(LocalDateTime.now());
    }

    @Override
    public boolean canAbandonMatch(Match match) {
        return true; // Can abandon a match that hasn't started
    }

    @Override
    public void abandonMatch(Match match) {
        log.info("Abandoning match before start: {}", match.getMatchName());
        match.setStatus(Match.MatchStatus.ABANDONED);
        match.setEndTime(LocalDateTime.now());
    }

    @Override
    public boolean canStartNewInnings(Match match) {
        return false; // Cannot start innings before match starts
    }

    @Override
    public void handleInningsCompletion(Match match) {
        // Nothing to do - no innings have started
        log.debug("No innings to handle in NOT_STARTED state");
    }
}
