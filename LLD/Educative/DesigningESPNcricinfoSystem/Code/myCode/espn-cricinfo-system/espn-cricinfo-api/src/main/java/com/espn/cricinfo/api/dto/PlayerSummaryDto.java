package com.espn.cricinfo.api.dto;

import com.espn.cricinfo.infrastructure.entity.PlayerEntity;

/**
 * DTO for player summary information.
 */
public record PlayerSummaryDto(
        Long id,
        String name,
        String fullName,
        String role,
        String nationality,
        Integer age,
        String battingStyle,
        String bowlingStyle
) {
    public static PlayerSummaryDto fromEntity(PlayerEntity entity) {
        return new PlayerSummaryDto(
                entity.getId(),
                entity.getName(),
                entity.getFullName(),
                entity.getRole(),
                entity.getNationality(),
                entity.getAge(),
                entity.getBattingStyle(),
                entity.getBowlingStyle()
        );
    }
}
