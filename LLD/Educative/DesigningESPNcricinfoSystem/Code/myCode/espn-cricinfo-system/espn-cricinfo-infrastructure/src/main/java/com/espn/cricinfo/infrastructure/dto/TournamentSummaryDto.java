package com.espn.cricinfo.infrastructure.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Summary DTO for Tournament information.
 */
@Data
@Builder
public class TournamentSummaryDto {
    private Long id;
    private String name;
    private String shortName;
    private String format;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String hostCountry;
    private Integer totalTeams;
    private Integer totalMatches;
    private Integer matchesCompleted;
    private Double prizeMoneyMillion;

    // Winner
    private TeamSummaryDto winner;
    private TeamSummaryDto runnerUp;
}
