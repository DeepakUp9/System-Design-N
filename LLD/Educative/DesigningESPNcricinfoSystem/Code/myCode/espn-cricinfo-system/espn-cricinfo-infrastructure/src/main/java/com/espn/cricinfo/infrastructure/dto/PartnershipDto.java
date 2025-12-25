package com.espn.cricinfo.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for batting partnership information.
 */
@Data
@Builder
public class PartnershipDto {
    private PlayerSummaryDto batsman1;
    private PlayerSummaryDto batsman2;
    private Integer runsScored;
    private Integer ballsPlayed;
    private Double runRate;
    private Integer wicketsAtStart;
    private String partnershipType; // Opening, Middle, etc.
}
