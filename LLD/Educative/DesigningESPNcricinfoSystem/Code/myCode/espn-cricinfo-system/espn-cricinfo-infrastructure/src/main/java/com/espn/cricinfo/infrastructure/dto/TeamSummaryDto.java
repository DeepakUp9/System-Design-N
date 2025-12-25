package com.espn.cricinfo.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Summary DTO for Team information.
 */
@Data
@Builder
public class TeamSummaryDto {
    private Long id;
    private String name;
    private String shortName;
    private String country;
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;

    // Key players
    private PlayerSummaryDto captain;
    private PlayerSummaryDto wicketKeeper;

    // Statistics
    private Integer totalMatchesPlayed;
    private Integer totalMatchesWon;
    private Double winPercentage;
}
