package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * State representing a player who is not currently batting.
 * This can be the initial state or after being dismissed.
 */
@Slf4j
public class PlayerNotBattingState implements PlayerState {

    @Override
    public String getStateName() {
        return "NOT_BATTING";
    }

    @Override
    public boolean canEnterField(Player player) {
        // Can enter field if not already on field
        return player != null;
    }

    @Override
    public void enterField(Player player) {
        if (!canEnterField(player)) {
            throw new IllegalStateException("Player cannot enter field from current state");
        }

        log.info("Player {} entering field", player.getName());
        // Additional field entry logic can be added here
    }

    @Override
    public boolean canStartBatting(Player player) {
        // Can start batting if player is a batsman and innings is active
        return player != null && player.isBatsman();
    }

    @Override
    public void startBatting(Player player) {
        if (!canStartBatting(player)) {
            throw new IllegalStateException("Player cannot start batting: not a batsman or invalid state");
        }

        log.info("Player {} starting to bat", player.getName());
        // Additional batting start logic can be added here
    }

    @Override
    public boolean canBeDismissed(Player player) {
        return false; // Cannot be dismissed if not batting
    }

    @Override
    public void getOut(Player player, String dismissalType) {
        throw new IllegalStateException("Cannot dismiss player: not currently batting");
    }

    @Override
    public boolean canStartBowling(Player player) {
        // Can start bowling if player is a bowler
        return player != null && player.isBowler();
    }

    @Override
    public void startBowling(Player player) {
        if (!canStartBowling(player)) {
            throw new IllegalStateException("Player cannot start bowling: not a bowler");
        }

        log.info("Player {} starting to bowl", player.getName());
        // Additional bowling start logic can be added here
    }

    @Override
    public boolean canLeaveField(Player player) {
        // Can leave field if currently on field
        return player != null;
    }

    @Override
    public void leaveField(Player player) {
        if (!canLeaveField(player)) {
            throw new IllegalStateException("Player cannot leave field from current state");
        }

        log.info("Player {} leaving field", player.getName());
        // Additional field exit logic can be added here
    }

    @Override
    public void handleInjury(Player player) {
        log.warn("Player {} injured while not batting", player.getName());
        // Handle injury - may need to leave field or be replaced
    }

    @Override
    public void handleRetirement(Player player) {
        log.info("Player {} retiring while not batting", player.getName());
        // Handle retirement - player leaves the match
    }
}
