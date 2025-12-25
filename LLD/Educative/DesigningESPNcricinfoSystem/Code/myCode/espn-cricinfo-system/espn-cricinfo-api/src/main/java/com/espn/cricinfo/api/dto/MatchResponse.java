package com.espn.cricinfo.api.dto;

import com.espn.cricinfo.domain.enums.MatchFormat;
import com.espn.cricinfo.infrastructure.entity.MatchEntity;

import java.time.LocalDateTime;

/**
 * DTO for match response data.
 */
public record MatchResponse(
        Long id,
        String matchName,
        MatchFormat format,
        String status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        TeamSummaryDto team1,
        TeamSummaryDto team2,
        TeamSummaryDto winner,
        VenueSummaryDto venue,
        TournamentSummaryDto tournament,
        Integer currentInningsNumber,
        Boolean isFirstInnings,
        Boolean isSecondInnings,
        Integer totalOvers,
        Integer totalBalls,
        String resultType,
        String resultDescription,
        PlayerSummaryDto manOfTheMatch,
        String weatherCondition,
        Double temperatureCelsius,
        Double humidityPercentage,
        Double windSpeedKmh,
        String umpire1Name,
        String umpire2Name,
        String thirdUmpireName,
        String matchRefereeName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MatchResponse fromEntity(MatchEntity entity) {
        return new MatchResponse(
                entity.getId(),
                entity.getMatchName(),
                entity.getFormat(),
                entity.getStatus().name(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getTeam1() != null ? TeamSummaryDto.fromEntity(entity.getTeam1()) : null,
                entity.getTeam2() != null ? TeamSummaryDto.fromEntity(entity.getTeam2()) : null,
                entity.getWinner() != null ? TeamSummaryDto.fromEntity(entity.getWinner()) : null,
                entity.getVenue() != null ? VenueSummaryDto.fromEntity(entity.getVenue()) : null,
                entity.getTournament() != null ? TournamentSummaryDto.fromEntity(entity.getTournament()) : null,
                entity.getCurrentInningsNumber(),
                entity.getIsFirstInnings(),
                entity.getIsSecondInnings(),
                entity.getTotalOvers(),
                entity.getTotalBalls(),
                entity.getResultType(),
                entity.getResultDescription(),
                null, // manOfTheMatch - would need additional mapping
                entity.getWeatherCondition(),
                entity.getTemperatureCelsius(),
                entity.getHumidityPercentage(),
                entity.getWindSpeedKmh(),
                entity.getUmpire1Name(),
                entity.getUmpire2Name(),
                entity.getThirdUmpireName(),
                entity.getMatchRefereeName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
