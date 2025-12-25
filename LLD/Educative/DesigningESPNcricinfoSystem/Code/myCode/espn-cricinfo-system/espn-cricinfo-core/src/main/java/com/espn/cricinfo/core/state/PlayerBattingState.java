package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * State representing a player who is currently batting.
 * Handles batting-related actions and transitions to out state.
 */
@Slf4j
public class PlayerBattingState implements PlayerState {

    @Override
    public String getStateName() {
        return "BATTING";
    }

    @Override
    public boolean canEnterField(Player player) {
        return false; // Already on field (batting)
    }

    @Override
    public void enterField(Player player) {
        throw new IllegalStateException("Player is already on field (batting)");
    }

    @Override
    public boolean canStartBatting(Player player) {
        return false; // Already batting
    }

    @Override
    public void startBatting(Player player) {
        throw new IllegalStateException("Player is already batting");
    }

    @Override
    public boolean canBeDismissed(Player player) {
        return true; // Can be dismissed while batting
    }

    @Override
    public void getOut(Player player, String dismissalType) {
        if (!canBeDismissed(player)) {
            throw new IllegalStateException("Player cannot be dismissed from current state");
        }

        log.info("Player {} dismissed: {}", player.getName(), dismissalType);
        // Additional dismissal logic can be added here
        // e.g., update player statistics, innings statistics
    }

    @Override
    public boolean canStartBowling(Player player) {
        return false; // Cannot start bowling while batting
    }

    @Override
    public void startBowling(Player player) {
        throw new IllegalStateException("Cannot start bowling while batting");
    }

    @Override
    public boolean canLeaveField(Player player) {
        return false; // Cannot leave field while batting (must be dismissed first)
    }

    @Override
    public void leaveField(Player player) {
        throw new IllegalStateException("Cannot leave field while batting - must be dismissed first");
    }

    @Override
    public void handleInjury(Player player) {
        log.warn("Player {} injured while batting", player.getName());
        // Handle injury while batting - may result in retirement or runner
        handleRetirement(player); // For simplicity, treat as retirement
    }

    @Override
    public void handleRetirement(Player player) {
        log.info("Player {} retiring while batting", player.getName());
        // Handle retirement while batting - counts as dismissal
        getOut(player, "RETIRED");
    }
}
