package com.espn.cricinfo.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for team statistics in a specific match.
 */
@Data
@Builder
public class TeamMatchStatsDto {
    private Long teamId;
    private String teamName;
    private Integer runsScored;
    private Integer wicketsLost;
    private Integer oversPlayed;
    private Double runRate;
    private Integer extrasConceded;
    private Integer boundariesHit;
    private Integer sixesHit;
}
