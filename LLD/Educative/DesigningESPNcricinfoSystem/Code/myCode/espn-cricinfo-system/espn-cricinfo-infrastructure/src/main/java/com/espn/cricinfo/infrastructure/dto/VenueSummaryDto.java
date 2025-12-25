package com.espn.cricinfo.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Summary DTO for Venue information.
 */
@Data
@Builder
public class VenueSummaryDto {
    private Long id;
    private String name;
    private String city;
    private String country;
    private Integer capacity;
    private String pitchType;
    private Double latitude;
    private Double longitude;

    // Statistics
    private Double averageFirstInningsScore;
    private Double averageRunsPerWicket;
    private String highestScore;
    private String lowestScore;
}
