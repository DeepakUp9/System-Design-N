package com.espn.cricinfo.api.dto;

import com.espn.cricinfo.infrastructure.entity.TournamentEntity;

import java.time.LocalDate;

/**
 * DTO for tournament summary information.
 */
public record TournamentSummaryDto(
        Long id,
        String name,
        String shortName,
        String format,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalTeams,
        Integer totalMatches,
        Integer matchesCompleted
) {
    public static TournamentSummaryDto fromEntity(TournamentEntity entity) {
        return new TournamentSummaryDto(
                entity.getId(),
                entity.getName(),
                entity.getShortName(),
                entity.getFormat().name(),
                entity.getStatus().name(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getTotalTeams(),
                entity.getTotalMatches(),
                entity.getMatchesCompleted()
        );
    }
}
