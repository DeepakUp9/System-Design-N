package com.espn.cricinfo.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Summary DTO for Player information.
 */
@Data
@Builder
public class PlayerSummaryDto {
    private Long id;
    private String name;
    private String fullName;
    private String role;
    private String nationality;
    private Integer age;
    private String battingStyle;
    private String bowlingStyle;

    // Statistics
    private Integer totalRuns;
    private Integer totalWickets;
    private Double battingAverage;
    private Double bowlingAverage;
}
