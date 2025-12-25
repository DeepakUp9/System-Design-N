package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Match;

/**
 * State interface for match lifecycle management.
 * Defines the behavior for different match states in the State Pattern.
 *
 * This interface handles transitions between match states like:
 * Not Started → In Progress → Paused → Completed → Abandoned
 */
public interface MatchState {

    /**
     * Gets the name of this state.
     *
     * @return state name
     */
    String getStateName();

    /**
     * Checks if the match can be started from this state.
     *
     * @param match the match to check
     * @return true if match can be started
     */
    boolean canStartMatch(Match match);

    /**
     * Starts the match from this state.
     * Should throw exception if transition is invalid.
     *
     * @param match the match to start
     * @throws IllegalStateException if transition is not allowed
     */
    void startMatch(Match match);

    /**
     * Checks if the match can be paused from this state.
     *
     * @param match the match to check
     * @return true if match can be paused
     */
    boolean canPauseMatch(Match match);

    /**
     * Pauses the match from this state.
     *
     * @param match the match to pause
     * @throws IllegalStateException if transition is not allowed
     */
    void pauseMatch(Match match);

    /**
     * Checks if the match can be resumed from this state.
     *
     * @param match the match to check
     * @return true if match can be resumed
     */
    boolean canResumeMatch(Match match);

    /**
     * Resumes the match from this state.
     *
     * @param match the match to resume
     * @throws IllegalStateException if transition is not allowed
     */
    void resumeMatch(Match match);

    /**
     * Checks if the match can be ended from this state.
     *
     * @param match the match to check
     * @return true if match can be ended
     */
    boolean canEndMatch(Match match);

    /**
     * Ends the match from this state.
     *
     * @param match the match to end
     * @throws IllegalStateException if transition is not allowed
     */
    void endMatch(Match match);

    /**
     * Checks if the match can be abandoned from this state.
     *
     * @param match the match to check
     * @return true if match can be abandoned
     */
    boolean canAbandonMatch(Match match);

    /**
     * Abandons the match from this state.
     *
     * @param match the match to abandon
     * @throws IllegalStateException if transition is not allowed
     */
    void abandonMatch(Match match);

    /**
     * Checks if new innings can be started from this state.
     *
     * @param match the match to check
     * @return true if new innings can be started
     */
    boolean canStartNewInnings(Match match);

    /**
     * Handles the completion of current innings.
     *
     * @param match the match with completed innings
     */
    void handleInningsCompletion(Match match);
}
