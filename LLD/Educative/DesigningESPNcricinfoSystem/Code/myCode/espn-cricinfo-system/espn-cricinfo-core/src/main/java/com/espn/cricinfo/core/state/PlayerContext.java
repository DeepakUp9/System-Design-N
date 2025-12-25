package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * Context class for the Player State Pattern.
 * Manages the current player state and handles state transitions.
 */
@Slf4j
public class PlayerContext {

    private PlayerState currentState;
    private final Player player;

    // Pre-defined state instances (singleton pattern for states)
    private static final PlayerState NOT_BATTING_STATE = new PlayerNotBattingState();
    private static final PlayerState BATTING_STATE = new PlayerBattingState();
    private static final PlayerState OUT_STATE = new PlayerOutState();
    private static final PlayerState BOWLING_STATE = new PlayerBowlingState();

    public PlayerContext(Player player) {
        this.player = player;
        // Initialize to not batting state
        this.currentState = NOT_BATTING_STATE;
        log.info("Player context initialized for: {}", player.getName());
    }

    /**
     * Gets the current state of the player.
     *
     * @return current player state
     */
    public PlayerState getCurrentState() {
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
    private void changeState(PlayerState newState) {
        log.info("Player {} transitioning from {} to {}",
                player.getName(), currentState.getStateName(), newState.getStateName());
        this.currentState = newState;
    }

    /**
     * Player enters the field.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void enterField() {
        if (currentState.canEnterField(player)) {
            currentState.enterField(player);
            // State remains the same, just confirms player is on field
        } else {
            throw new IllegalStateException("Cannot enter field from current state: " + currentState.getStateName());
        }
    }

    /**
     * Player starts batting.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void startBatting() {
        if (currentState.canStartBatting(player)) {
            currentState.startBatting(player);
            changeState(BATTING_STATE);
        } else {
            throw new IllegalStateException("Cannot start batting from current state: " + currentState.getStateName());
        }
    }

    /**
     * Player gets out (dismissed).
     *
     * @param dismissalType the type of dismissal
     * @throws IllegalStateException if transition is not allowed
     */
    public void getOut(String dismissalType) {
        if (currentState.canBeDismissed(player)) {
            currentState.getOut(player, dismissalType);
            changeState(OUT_STATE);
        } else {
            throw new IllegalStateException("Cannot dismiss player from current state: " + currentState.getStateName());
        }
    }

    /**
     * Player starts bowling.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void startBowling() {
        if (currentState.canStartBowling(player)) {
            currentState.startBowling(player);
            changeState(BOWLING_STATE);
        } else {
            throw new IllegalStateException("Cannot start bowling from current state: " + currentState.getStateName());
        }
    }

    /**
     * Player leaves the field.
     *
     * @throws IllegalStateException if transition is not allowed
     */
    public void leaveField() {
        if (currentState.canLeaveField(player)) {
            currentState.leaveField(player);
            changeState(NOT_BATTING_STATE); // Return to not batting state
        } else {
            throw new IllegalStateException("Cannot leave field from current state: " + currentState.getStateName());
        }
    }

    /**
     * Handles injury event.
     */
    public void handleInjury() {
        currentState.handleInjury(player);
    }

    /**
     * Handles retirement event.
     */
    public void handleRetirement() {
        currentState.handleRetirement(player);
    }

    /**
     * Checks if the player is currently batting.
     *
     * @return true if batting
     */
    public boolean isBatting() {
        return currentState instanceof PlayerBattingState;
    }

    /**
     * Checks if the player is currently bowling.
     *
     * @return true if bowling
     */
    public boolean isBowling() {
        return currentState instanceof PlayerBowlingState;
    }

    /**
     * Checks if the player is out.
     *
     * @return true if out
     */
    public boolean isOut() {
        return currentState instanceof PlayerOutState;
    }

    /**
     * Checks if the player can bat.
     *
     * @return true if can bat
     */
    public boolean canBat() {
        return currentState.canStartBatting(player);
    }

    /**
     * Checks if the player can bowl.
     *
     * @return true if can bowl
     */
    public boolean canBowl() {
        return currentState.canStartBowling(player);
    }

    /**
     * Gets the underlying player entity.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the player's name.
     *
     * @return player name
     */
    public String getPlayerName() {
        return player.getName();
    }

    /**
     * Gets the player's role.
     *
     * @return player role
     */
    public String getPlayerRole() {
        return player.getRole();
    }
}
