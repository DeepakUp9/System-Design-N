package com.espn.cricinfo.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for match phase statistics (powerplay, middle, death overs).
 */
@Data
@Builder
public class PhaseStatsDto {
    private String phaseName; // Powerplay, Middle Overs, Death Overs
    private Integer oversRangeStart;
    private Integer oversRangeEnd;
    private Integer runsScored;
    private Integer wicketsLost;
    private Integer ballsBowled;
    private Double runRate;
    private Integer boundaries;
    private Integer sixes;
}
