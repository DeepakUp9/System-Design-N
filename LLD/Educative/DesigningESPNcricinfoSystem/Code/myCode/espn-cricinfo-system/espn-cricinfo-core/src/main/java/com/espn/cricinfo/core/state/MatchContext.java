package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Match;
import lombok.extern.slf4j.Slf4j;

/**
 * Context class for the Match State Pattern.
 * Manages the current match state and handles state transitions.
 *
 * This class encapsulates the state management logic and provides
 * a clean interface for match lifecycle operations.
 */
@Slf4j
public class MatchContext {

    private MatchState currentState;
    private final Match match;

    // Pre-defined state instances (singleton pattern for states)
    private static final MatchState NOT_STARTED_STATE = new MatchNotStartedState();
    private static final MatchState IN_PROGRESS_STATE = new MatchInProgressState();
    private static final MatchState PAUSED_STATE = new MatchPausedState();
    private static final MatchState COMPLETED_STATE = new MatchCompletedState();
    private static final MatchState ABANDONED_STATE = new MatchAbandonedState();

    public MatchContext(Match match) {
        this.match = match;
        // Initialize to not started state
        this.currentState = NOT_STARTED_STATE;
        log.info("Match context initialized for: {}", match.getMatchName());
    }

    /**
     * Gets the current state of the match.
     *
     * @return current match state
     */
    public MatchState getCurrentState() {
        return currentState;
    }

    /**
     * Gets the current state name.
     *
     * @return state name
     */
    public String getCurrentStateName() {
        return currentState.getStateName();
    }

    /**
     * Transitions to a new state.
     *
     * @param newState the new state to transition to
     */
    private void changeState(MatchState newState) {
        log.info("Match {} transitioning from {} to {}",
                match.getMatchName(), currentState.getStateName(), newState.getStateName());
        this.currentState = newState;
    }

    /**
     * Starts the match.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void startMatch() {
        if (currentState.canStartMatch(match)) {
            currentState.startMatch(match);
            changeState(IN_PROGRESS_STATE);
        } else {
            throw new IllegalStateException("Cannot start match from current state: " + currentState.getStateName());
        }
    }

    /**
     * Pauses the match.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void pauseMatch() {
        if (currentState.canPauseMatch(match)) {
            currentState.pauseMatch(match);
            changeState(PAUSED_STATE);
        } else {
            throw new IllegalStateException("Cannot pause match from current state: " + currentState.getStateName());
        }
    }

    /**
     * Resumes the match.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void resumeMatch() {
        if (currentState.canResumeMatch(match)) {
            currentState.resumeMatch(match);
            changeState(IN_PROGRESS_STATE);
        } else {
            throw new IllegalStateException("Cannot resume match from current state: " + currentState.getStateName());
        }
    }

    /**
     * Ends the match.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void endMatch() {
        if (currentState.canEndMatch(match)) {
            currentState.endMatch(match);
            changeState(COMPLETED_STATE);
        } else {
            throw new IllegalStateException("Cannot end match from current state: " + currentState.getStateName());
        }
    }

    /**
     * Abandons the match.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void abandonMatch() {
        if (currentState.canAbandonMatch(match)) {
            currentState.abandonMatch(match);
            changeState(ABANDONED_STATE);
        } else {
            throw new IllegalStateException("Cannot abandon match from current state: " + currentState.getStateName());
        }
    }

    /**
     * Checks if new innings can be started.
     *
     * @return true if new innings can be started
     */
    public boolean canStartNewInnings() {
        return currentState.canStartNewInnings(match);
    }

    /**
     * Handles innings completion.
     * This may trigger state transitions.
     */
    public void handleInningsCompletion() {
        currentState.handleInningsCompletion(match);

        // Check if match should transition to completed after innings completion
        if (currentState instanceof MatchInProgressState &&
            ((MatchInProgressState) currentState).canEndMatch(match)) {
            endMatch();
        }
    }

    /**
     * Checks if the match is in a terminal state.
     *
     * @return true if match is completed or abandoned
     */
    public boolean isTerminalState() {
        return currentState instanceof MatchCompletedState ||
               currentState instanceof MatchAbandonedState;
    }

    /**
     * Checks if the match is active (in progress or paused).
     *
     * @return true if match is active
     */
    public boolean isActive() {
        return currentState instanceof MatchInProgressState ||
               currentState instanceof MatchPausedState;
    }

    /**
     * Gets the underlying match entity.
     *
     * @return the match
     */
    public Match getMatch() {
        return match;
    }
}
