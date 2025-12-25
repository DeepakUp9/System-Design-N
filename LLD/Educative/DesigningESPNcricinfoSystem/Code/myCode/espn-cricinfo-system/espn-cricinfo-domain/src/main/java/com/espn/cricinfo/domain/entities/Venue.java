package com.espn.cricinfo.domain.entities;

import lombok.Builder;
import lombok.Data;

/**
 * Entity representing a cricket venue.
 */
@Data
@Builder
public class Venue {
    private Long id;
    private String name;
    private String city;
    private String country;
    private int capacity;
    private String pitchType; // GRASS, TURF, etc.
    private Double averageFirstInningsScore;
    private Double averageChaseSuccessRate;
}