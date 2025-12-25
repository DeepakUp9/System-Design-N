package com.espn.cricinfo.domain.entities;

import com.espn.cricinfo.domain.valueobjects.PlayerStats;
import lombok.Builder;
import lombok.Data;

/**
 * Entity representing a cricket player.
 */
@Data
@Builder
public class Player {
    private Long id;
    private String name;
    private String fullName;
    private String role; // BATSMAN, BOWLER, ALL_ROUNDER, WICKET_KEEPER
    private String nationality;
    private int age;
    private PlayerStats careerStats;

    public boolean isWicketKeeper() {
        return "WICKET_KEEPER".equals(role);
    }

    public boolean isBatsman() {
        return "BATSMAN".equals(role) || "ALL_ROUNDER".equals(role);
    }

    public boolean isBowler() {
        return "BOWLER".equals(role) || "ALL_ROUNDER".equals(role);
    }
}