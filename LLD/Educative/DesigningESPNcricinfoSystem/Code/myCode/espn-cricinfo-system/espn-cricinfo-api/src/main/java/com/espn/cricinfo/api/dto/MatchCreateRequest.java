package com.espn.cricinfo.api.dto;

import com.espn.cricinfo.domain.enums.MatchFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO for creating a new match.
 */
public record MatchCreateRequest(
        @NotBlank(message = "Match name is required")
        String matchName,

        @NotNull(message = "Match format is required")
        MatchFormat format,

        @NotNull(message = "Team 1 ID is required")
        Long team1Id,

        @NotNull(message = "Team 2 ID is required")
        Long team2Id,

        @NotNull(message = "Venue ID is required")
        Long venueId,

        Long tournamentId,

        @NotNull(message = "Start time is required")
        LocalDateTime startTime
) {}
