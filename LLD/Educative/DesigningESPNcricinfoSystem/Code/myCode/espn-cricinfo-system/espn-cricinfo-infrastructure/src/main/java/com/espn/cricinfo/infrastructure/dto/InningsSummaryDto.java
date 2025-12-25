package com.espn.cricinfo.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Summary DTO for Innings information.
 */
@Data
@Builder
public class InningsSummaryDto {
    private Integer inningsNumber;
    private TeamSummaryDto battingTeam;
    private TeamSummaryDto bowlingTeam;

    // Statistics
    private Integer totalRuns;
    private Integer totalWickets;
    private Integer totalOvers;
    private Integer totalBalls;
    private Double runRate;

    // Target information
    private Integer targetRuns;
    private Double targetOvers;
    private Integer runsRemaining;
    private Integer wicketsRemaining;
    private Double requiredRunRate;

    // Status
    private Boolean isCompleted;
    private Boolean isDeclared;
    private Boolean isAllOut;

    // Key performances
    private PlayerMatchStatsDto topScorer;
    private PlayerMatchStatsDto bestBowler;
}
