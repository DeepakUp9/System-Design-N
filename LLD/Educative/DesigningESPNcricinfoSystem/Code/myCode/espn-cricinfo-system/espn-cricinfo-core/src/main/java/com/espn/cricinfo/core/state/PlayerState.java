package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Player;

/**
 * State interface for player status management in cricket.
 * Defines the behavior for different player states in the State Pattern.
 *
 * This interface handles transitions between player states like:
 * Not Batting → Batting → Out → Left Field
 */
public interface PlayerState {

    /**
     * Gets the name of this state.
     *
     * @return state name
     */
    String getStateName();

    /**
     * Checks if the player can enter the field from this state.
     *
     * @param player the player to check
     * @return true if player can enter field
     */
    boolean canEnterField(Player player);

    /**
     * Player enters the field from this state.
     *
     * @param player the player entering field
     * @throws IllegalStateException if transition is not allowed
     */
    void enterField(Player player);

    /**
     * Checks if the player can start batting from this state.
     *
     * @param player the player to check
     * @return true if player can start batting
     */
    boolean canStartBatting(Player player);

    /**
     * Player starts batting from this state.
     *
     * @param player the player starting to bat
     * @throws IllegalStateException if transition is not allowed
     */
    void startBatting(Player player);

    /**
     * Checks if the player can be dismissed from this state.
     *
     * @param player the player to check
     * @return true if player can be dismissed
     */
    boolean canBeDismissed(Player player);

    /**
     * Player gets out (dismissed) from this state.
     *
     * @param player the player getting out
     * @param dismissalType the type of dismissal
     * @throws IllegalStateException if transition is not allowed
     */
    void getOut(Player player, String dismissalType);

    /**
     * Checks if the player can start bowling from this state.
     *
     * @param player the player to check
     * @return true if player can start bowling
     */
    boolean canStartBowling(Player player);

    /**
     * Player starts bowling from this state.
     *
     * @param player the player starting to bowl
     * @throws IllegalStateException if transition is not allowed
     */
    void startBowling(Player player);

    /**
     * Checks if the player can leave the field from this state.
     *
     * @param player the player to check
     * @return true if player can leave field
     */
    boolean canLeaveField(Player player);

    /**
     * Player leaves the field from this state.
     *
     * @param player the player leaving field
     * @throws IllegalStateException if transition is not allowed
     */
    void leaveField(Player player);

    /**
     * Handles injury event for the player.
     *
     * @param player the injured player
     */
    void handleInjury(Player player);

    /**
     * Handles retirement event for the player.
     *
     * @param player the retiring player
     */
    void handleRetirement(Player player);
}
