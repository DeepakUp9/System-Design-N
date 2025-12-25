package com.espn.cricinfo.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for player performance in a specific match.
 */
@Data
@Builder
public class PlayerMatchStatsDto {
    private Long playerId;
    private String playerName;
    private String role;

    // Batting stats
    private Integer runsScored;
    private Integer ballsFaced;
    private Integer boundaries;
    private Integer sixes;
    private Double strikeRate;
    private String dismissalType;

    // Bowling stats
    private Integer oversBowled;
    private Integer maidens;
    private Integer wicketsTaken;
    private Integer runsConceded;
    private Double economyRate;
}
