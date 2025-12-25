package com.espn.cricinfo.api.dto;

import com.espn.cricinfo.infrastructure.entity.VenueEntity;

/**
 * DTO for venue summary information.
 */
public record VenueSummaryDto(
        Long id,
        String name,
        String city,
        String country,
        Integer capacity,
        String pitchType,
        Double latitude,
        Double longitude
) {
    public static VenueSummaryDto fromEntity(VenueEntity entity) {
        return new VenueSummaryDto(
                entity.getId(),
                entity.getName(),
                entity.getCity(),
                entity.getCountry(),
                entity.getCapacity(),
                entity.getPitchType(),
                entity.getLatitude(),
                entity.getLongitude()
        );
    }
}
