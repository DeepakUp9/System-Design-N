package com.espn.cricinfo.api.dto;

import com.espn.cricinfo.infrastructure.entity.TeamEntity;

/**
 * DTO for team summary information.
 */
public record TeamSummaryDto(
        Long id,
        String name,
        String shortName,
        String country,
        String logoUrl,
        String primaryColor,
        String secondaryColor,
        Integer totalMatchesPlayed,
        Integer totalMatchesWon,
        Double winPercentage
) {
    public static TeamSummaryDto fromEntity(TeamEntity entity) {
        return new TeamSummaryDto(
                entity.getId(),
                entity.getName(),
                entity.getShortName(),
                entity.getCountry(),
                entity.getLogoUrl(),
                entity.getPrimaryColor(),
                entity.getSecondaryColor(),
                entity.getTotalMatchesPlayed(),
                entity.getTotalMatchesWon(),
                entity.getTotalMatchesPlayed() > 0 ?
                        (double) entity.getTotalMatchesWon() / entity.getTotalMatchesPlayed() * 100 : 0.0
        );
    }
}
