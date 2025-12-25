package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * State representing a player who is currently bowling.
 * Handles bowling-related actions and transitions.
 */
@Slf4j
public class PlayerBowlingState implements PlayerState {

    @Override
    public String getStateName() {
        return "BOWLING";
    }

    @Override
    public boolean canEnterField(Player player) {
        return false; // Already on field (bowling)
    }

    @Override
    public void enterField(Player player) {
        throw new IllegalStateException("Player is already on field (bowling)");
    }

    @Override
    public boolean canStartBatting(Player player) {
        return false; // Cannot start batting while bowling
    }

    @Override
    public void startBatting(Player player) {
        throw new IllegalStateException("Cannot start batting while bowling");
    }

    @Override
    public boolean canBeDismissed(Player player) {
        return false; // Cannot be dismissed while bowling (not batting)
    }

    @Override
    public void getOut(Player player, String dismissalType) {
        throw new IllegalStateException("Cannot dismiss player while bowling - not batting");
    }

    @Override
    public boolean canStartBowling(Player player) {
        return false; // Already bowling
    }

    @Override
    public void startBowling(Player player) {
        throw new IllegalStateException("Player is already bowling");
    }

    @Override
    public boolean canLeaveField(Player player) {
        // Can leave field after completing bowling spell
        return player != null;
    }

    @Override
    public void leaveField(Player player) {
        if (!canLeaveField(player)) {
            throw new IllegalStateException("Player cannot leave field from current state");
        }

        log.info("Player {} finishing bowling spell", player.getName());
        // Additional field exit logic can be added here
        // e.g., update bowling statistics
    }

    @Override
    public void handleInjury(Player player) {
        log.warn("Player {} injured while bowling", player.getName());
        // Handle injury while bowling - may need to stop bowling immediately
        leaveField(player); // Stop bowling due to injury
    }

    @Override
    public void handleRetirement(Player player) {
        log.info("Player {} retiring while bowling", player.getName());
        // Handle retirement while bowling - stop bowling and leave match
        leaveField(player);
    }
}
