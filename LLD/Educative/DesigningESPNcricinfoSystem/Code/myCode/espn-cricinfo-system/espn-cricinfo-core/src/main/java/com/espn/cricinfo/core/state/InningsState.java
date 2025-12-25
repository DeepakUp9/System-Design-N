package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Innings;

/**
 * State interface for innings lifecycle management.
 * Defines the behavior for different innings states in the State Pattern.
 *
 * This interface handles transitions between innings states like:
 * Not Started → In Progress → Completed → Declared (Test cricket)
 */
public interface InningsState {

    /**
     * Gets the name of this state.
     *
     * @return state name
     */
    String getStateName();

    /**
     * Checks if the innings can be started from this state.
     *
     * @param innings the innings to check
     * @return true if innings can be started
     */
    boolean canStartInnings(Innings innings);

    /**
     * Starts the innings from this state.
     *
     * @param innings the innings to start
     * @throws IllegalStateException if transition is not allowed
     */
    void startInnings(Innings innings);

    /**
     * Checks if a ball can be processed in this state.
     *
     * @param innings the innings to check
     * @param ball the ball to be processed
     * @return true if ball can be processed
     */
    boolean canProcessBall(Innings innings, Ball ball);

    /**
     * Processes a ball in this innings state.
     *
     * @param innings the innings
     * @param ball the ball to process
     * @throws IllegalStateException if ball cannot be processed
     */
    void processBall(Innings innings, Ball ball);

    /**
     * Checks if the innings can be completed from this state.
     *
     * @param innings the innings to check
     * @return true if innings can be completed
     */
    boolean canCompleteInnings(Innings innings);

    /**
     * Completes the innings from this state.
     *
     * @param innings the innings to complete
     * @throws IllegalStateException if transition is not allowed
     */
    void completeInnings(Innings innings);

    /**
     * Checks if the innings can be declared from this state (Test cricket only).
     *
     * @param innings the innings to check
     * @return true if innings can be declared
     */
    boolean canDeclareInnings(Innings innings);

    /**
     * Declares the innings from this state (Test cricket only).
     *
     * @param innings the innings to declare
     * @throws IllegalStateException if transition is not allowed
     */
    void declareInnings(Innings innings);

    /**
     * Checks if wickets can be taken in this state.
     *
     * @param innings the innings to check
     * @return true if wickets can be taken
     */
    boolean canTakeWicket(Innings innings);

    /**
     * Handles wicket events in this state.
     *
     * @param innings the innings
     * @param wicketType the type of wicket
     */
    void handleWicket(Innings innings, String wicketType);
}
