package com.espn.cricinfo.api.dto;

import com.espn.cricinfo.domain.enums.BallType;
import jakarta.validation.constraints.*;

/**
 * DTO for recording a ball delivery.
 */
public record BallRecordRequest(
        @NotNull(message = "Over number is required")
        @Min(value = 0, message = "Over number must be non-negative")
        Integer overNumber,

        @NotNull(message = "Ball number is required")
        @Min(value = 1, message = "Ball number must be at least 1")
        @Max(value = 6, message = "Ball number must not exceed 6")
        Integer ballNumber,

        @NotNull(message = "Ball type is required")
        BallType ballType,

        @NotNull(message = "Runs scored is required")
        @Min(value = 0, message = "Runs scored must be non-negative")
        @Max(value = 6, message = "Runs scored must not exceed 6 for legal deliveries")
        Integer runsScored,

        @NotNull(message = "Wicket flag is required")
        Boolean isWicket,

        @NotNull(message = "Boundary flag is required")
        Boolean isBoundary,

        @NotNull(message = "Six flag is required")
        Boolean isSix,

        @NotBlank(message = "Bowler name is required")
        @Size(max = 100, message = "Bowler name cannot exceed 100 characters")
        String bowlerName,

        @NotBlank(message = "Batsman name is required")
        @Size(max = 100, message = "Batsman name cannot exceed 100 characters")
        String batsmanName,

        @Size(max = 500, message = "Commentary cannot exceed 500 characters")
        String commentary
) {}
