package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Innings;
import lombok.extern.slf4j.Slf4j;

/**
 * Context class for the Innings State Pattern.
 * Manages the current innings state and handles state transitions.
 */
@Slf4j
public class InningsContext {

    private InningsState currentState;
    private final Innings innings;

    // Pre-defined state instances (singleton pattern for states)
    private static final InningsState NOT_STARTED_STATE = new InningsNotStartedState();
    private static final InningsState IN_PROGRESS_STATE = new InningsInProgressState();
    private static final InningsState COMPLETED_STATE = new InningsCompletedState();
    private static final InningsState DECLARED_STATE = new InningsDeclaredState();

    public InningsContext(Innings innings) {
        this.innings = innings;
        // Initialize to not started state
        this.currentState = NOT_STARTED_STATE;
        log.info("Innings context initialized for innings {}", innings.getInningsNumber());
    }

    /**
     * Gets the current state of the innings.
     *
     * @return current innings state
     */
    public InningsState getCurrentState() {
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
    private void changeState(InningsState newState) {
        log.info("Innings {} transitioning from {} to {}",
                innings.getInningsNumber(), currentState.getStateName(), newState.getStateName());
        this.currentState = newState;
    }

    /**
     * Starts the innings.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void startInnings() {
        if (currentState.canStartInnings(innings)) {
            currentState.startInnings(innings);
            changeState(IN_PROGRESS_STATE);
        } else {
            throw new IllegalStateException("Cannot start innings from current state: " + currentState.getStateName());
        }
    }

    /**
     * Processes a ball in the innings.
     *
     * @param ball the ball to process
     * @throws IllegalStateException if ball cannot be processed
     */
    public void processBall(Ball ball) {
        if (currentState.canProcessBall(innings, ball)) {
            currentState.processBall(innings, ball);

            // Check if innings should transition to completed after ball processing
            if (currentState instanceof InningsInProgressState &&
                ((InningsInProgressState) currentState).canCompleteInnings(innings)) {
                completeInnings();
            }
        } else {
            throw new IllegalStateException("Cannot process ball from current state: " + currentState.getStateName());
        }
    }

    /**
     * Completes the innings.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void completeInnings() {
        if (currentState.canCompleteInnings(innings)) {
            currentState.completeInnings(innings);
            changeState(COMPLETED_STATE);
        } else {
            throw new IllegalStateException("Cannot complete innings from current state: " + currentState.getStateName());
        }
    }

    /**
     * Declares the innings (Test cricket only).
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void declareInnings() {
        if (currentState.canDeclareInnings(innings)) {
            currentState.declareInnings(innings);
            changeState(DECLARED_STATE);
        } else {
            throw new IllegalStateException("Cannot declare innings from current state: " + currentState.getStateName());
        }
    }

    /**
     * Handles a wicket event.
     *
     * @param wicketType the type of wicket
     * @throws IllegalStateException if wicket cannot be taken
     */
    public void handleWicket(String wicketType) {
        if (currentState.canTakeWicket(innings)) {
            currentState.handleWicket(innings, wicketType);
        } else {
            throw new IllegalStateException("Cannot take wicket from current state: " + currentState.getStateName());
        }
    }

    /**
     * Checks if the innings is active (in progress).
     *
     * @return true if innings is active
     */
    public boolean isActive() {
        return currentState instanceof InningsInProgressState;
    }

    /**
     * Checks if the innings is in a terminal state.
     *
     * @return true if innings is completed or declared
     */
    public boolean isTerminalState() {
        return currentState instanceof InningsCompletedState ||
               currentState instanceof InningsDeclaredState;
    }

    /**
     * Checks if innings can be declared.
     *
     * @return true if innings can be declared
     */
    public boolean canDeclareInnings() {
        return currentState.canDeclareInnings(innings);
    }

    /**
     * Gets the underlying innings entity.
     *
     * @return the innings
     */
    public Innings getInnings() {
        return innings;
    }

    /**
     * Gets the current run rate.
     *
     * @return run rate (runs per over)
     */
    public double getCurrentRunRate() {
        return innings.getRunRate();
    }

    /**
     * Gets the total runs scored.
     *
     * @return total runs
     */
    public int getTotalRuns() {
        return innings.getTotalRuns();
    }

    /**
     * Gets the total wickets fallen.
     *
     * @return total wickets
     */
    public int getTotalWickets() {
        return innings.getTotalWickets();
    }

    /**
     * Checks if the team is all out.
     *
     * @return true if all out
     */
    public boolean isAllOut() {
        return innings.isAllOut();
    }
}
