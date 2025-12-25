package com.espn.cricinfo.infrastructure.dto;

import com.espn.cricinfo.domain.enums.MatchFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Match information.
 * Used for API responses and data exchange.
 */
@Data
@Builder
public class MatchDto {

    private Long id;
    private String matchName;
    private MatchFormat format;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Teams
    private TeamSummaryDto team1;
    private TeamSummaryDto team2;
    private TeamSummaryDto winner;

    // Venue
    private VenueSummaryDto venue;

    // Tournament
    private TournamentSummaryDto tournament;

    // Match progress
    private Integer currentInningsNumber;
    private Boolean isFirstInnings;
    private Boolean isSecondInnings;
    private Integer totalOvers;
    private Integer totalBalls;

    // Results
    private String resultType;
    private String resultDescription;
    private PlayerSummaryDto manOfTheMatch;

    // Weather
    private String weatherCondition;
    private Double temperatureCelsius;
    private Double humidityPercentage;
    private Double windSpeedKmh;

    // Officials
    private String umpire1Name;
    private String umpire2Name;
    private String thirdUmpireName;
    private String matchRefereeName;

    // Statistics
    private MatchStatisticsDto statistics;

    // Innings (summary)
    private List<InningsSummaryDto> innings;

    // Live data
    private Boolean isLive;
    private LocalDateTime lastUpdated;

    /**
     * Check if match is currently live.
     */
    public Boolean getIsLive() {
        return "IN_PROGRESS".equals(status) || "PAUSED".equals(status);
    }
}
