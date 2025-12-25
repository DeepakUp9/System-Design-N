package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * State representing a player who has been dismissed (out).
 * Player cannot bat again in this innings but can still field/bowl.
 */
@Slf4j
public class PlayerOutState implements PlayerState {

    @Override
    public String getStateName() {
        return "OUT";
    }

    @Override
    public boolean canEnterField(Player player) {
        return false; // Already on field
    }

    @Override
    public void enterField(Player player) {
        throw new IllegalStateException("Player is already on field");
    }

    @Override
    public boolean canStartBatting(Player player) {
        return false; // Cannot bat again in this innings
    }

    @Override
    public void startBatting(Player player) {
        throw new IllegalStateException("Player cannot bat: already dismissed in this innings");
    }

    @Override
    public boolean canBeDismissed(Player player) {
        return false; // Already out
    }

    @Override
    public void getOut(Player player, String dismissalType) {
        throw new IllegalStateException("Player is already out");
    }

    @Override
    public boolean canStartBowling(Player player) {
        // Can bowl even if out, as long as player is a bowler
        return player != null && player.isBowler();
    }

    @Override
    public void startBowling(Player player) {
        if (!canStartBowling(player)) {
            throw new IllegalStateException("Player cannot bowl: not a bowler or invalid state");
        }

        log.info("Player {} (out) starting to bowl", player.getName());
        // Additional bowling start logic can be added here
    }

    @Override
    public boolean canLeaveField(Player player) {
        // Can leave field if no longer needed (e.g., innings ended)
        return player != null;
    }

    @Override
    public void leaveField(Player player) {
        if (!canLeaveField(player)) {
            throw new IllegalStateException("Player cannot leave field from current state");
        }

        log.info("Player {} (out) leaving field", player.getName());
        // Additional field exit logic can be added here
    }

    @Override
    public void handleInjury(Player player) {
        log.warn("Player {} (out) injured", player.getName());
        // Handle injury - may need medical attention
    }

    @Override
    public void handleRetirement(Player player) {
        log.info("Player {} (out) retiring from match", player.getName());
        // Handle retirement - player leaves the match completely
    }
}
