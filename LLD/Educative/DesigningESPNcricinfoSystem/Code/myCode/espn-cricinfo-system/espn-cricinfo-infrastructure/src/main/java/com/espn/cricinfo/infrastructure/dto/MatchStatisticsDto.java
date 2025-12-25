package com.espn.cricinfo.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for match statistics and analytics.
 */
@Data
@Builder
public class MatchStatisticsDto {
    // Overall statistics
    private Integer totalRuns;
    private Integer totalWickets;
    private Integer totalOvers;
    private Integer totalBalls;
    private Double runRate;

    // Team statistics
    private TeamMatchStatsDto team1Stats;
    private TeamMatchStatsDto team2Stats;

    // Player performances
    private PlayerMatchStatsDto bestBatsman;
    private PlayerMatchStatsDto bestBowler;

    // Match analysis
    private Integer boundaries;
    private Integer sixes;
    private Integer dotBalls;
    private Integer wicketsByBowling;
    private Integer wicketsByCatching;
    private Integer wicketsByRunOut;

    // Partnership analysis
    private PartnershipDto highestPartnership;
    private Double averagePartnership;

    // Phase analysis
    private PhaseStatsDto powerplayStats;
    private PhaseStatsDto middleOversStats;
    private PhaseStatsDto deathOversStats;
}
