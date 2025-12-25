package com.espn.cricinfo.api.dto;

import com.espn.cricinfo.domain.enums.MatchFormat;

import java.time.LocalDateTime;

/**
 * DTO for updating match details.
 */
public record MatchUpdateRequest(
        String matchName,
        MatchFormat format,
        LocalDateTime startTime,
        String weatherCondition,
        Double temperatureCelsius,
        Double humidityPercentage,
        Double windSpeedKmh,
        String umpire1Name,
        String umpire2Name,
        String thirdUmpireName,
        String matchRefereeName
) {}
