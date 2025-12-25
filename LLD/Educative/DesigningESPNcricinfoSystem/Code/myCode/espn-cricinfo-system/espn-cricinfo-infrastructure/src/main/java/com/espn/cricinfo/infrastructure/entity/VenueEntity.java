package com.espn.cricinfo.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

/**
 * JPA entity representing a cricket venue/stadium.
 * Contains venue information and performance statistics.
 */
@Entity
@Table(name = "venues", indexes = {
    @Index(name = "idx_venue_city", columnList = "city"),
    @Index(name = "idx_venue_country", columnList = "country"),
    @Index(name = "idx_venue_active", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VenueEntity extends BaseEntity {

    @NotBlank(message = "Venue name is required")
    @Size(min = 2, max = 200, message = "Venue name must be between 2 and 200 characters")
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters")
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Min(value = 100, message = "Capacity must be at least 100")
    @Max(value = 150000, message = "Capacity must not exceed 150,000")
    @Column(name = "capacity")
    private Integer capacity;

    @Size(max = 50, message = "Pitch type cannot exceed 50 characters")
    @Column(name = "pitch_type", length = 50)
    private String pitchType; // GRASS, TURF, DROPS, etc.

    @Size(max = 100, message = "Ownership cannot exceed 100 characters")
    @Column(name = "ownership", length = 100)
    private String ownership;

    @Column(name = "year_opened")
    private Integer yearOpened;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // Venue Statistics
    @Column(name = "total_matches_hosted", nullable = false)
    @Builder.Default
    private Integer totalMatchesHosted = 0;

    @DecimalMin(value = "0.0", message = "Average first innings score must be non-negative")
    @DecimalMax(value = "500.0", message = "Average first innings score must not exceed 500")
    @Column(name = "avg_first_innings_score", precision = 5, scale = 2)
    private Double averageFirstInningsScore;

    @DecimalMin(value = "0.0", message = "Average chase success rate must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Average chase success rate must be between 0 and 100")
    @Column(name = "avg_chase_success_rate", precision = 5, scale = 2)
    private Double averageChaseSuccessRate;

    @DecimalMin(value = "0.0", message = "Average runs per wicket must be non-negative")
    @DecimalMax(value = "50.0", message = "Average runs per wicket must not exceed 50")
    @Column(name = "avg_runs_per_wicket", precision = 4, scale = 2)
    private Double averageRunsPerWicket;

    @Size(max = 20, message = "Highest score cannot exceed 20 characters")
    @Column(name = "highest_score", length = 20)
    private String highestScore; // e.g., "450/8"

    @Size(max = 20, message = "Lowest score cannot exceed 20 characters")
    @Column(name = "lowest_score", length = 20)
    private String lowestScore; // e.g., "45"

    // Pitch characteristics
    @DecimalMin(value = "0.0", message = "Grass coverage must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Grass coverage must be between 0 and 100")
    @Column(name = "grass_coverage_percentage", precision = 5, scale = 2)
    private Double grassCoveragePercentage;

    @Min(value = 0, message = "Bounce rating must be between 0 and 10")
    @Max(value = 10, message = "Bounce rating must be between 0 and 10")
    @Column(name = "bounce_rating")
    private Integer bounceRating; // 1-10 scale

    @Min(value = 0, message = "Pace rating must be between 0 and 10")
    @Max(value = 10, message = "Pace rating must be between 0 and 10")
    @Column(name = "pace_rating")
    private Integer paceRating; // 1-10 scale

    @Min(value = 0, message = "Spin rating must be between 0 and 10")
    @Max(value = 10, message = "Spin rating must be between 0 and 10")
    @Column(name = "spin_rating")
    private Integer spinRating; // 1-10 scale

    @Min(value = 0, message = "Outfield rating must be between 0 and 10")
    @Max(value = 10, message = "Outfield rating must be between 0 and 10")
    @Column(name = "outfield_rating")
    private Integer outfieldRating; // 1-10 scale

    // Weather and geographical data
    @Size(max = 50, message = "Time zone cannot exceed 50 characters")
    @Column(name = "time_zone", length = 50)
    private String timeZone;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @Column(name = "latitude", precision = 10, scale = 8)
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @Column(name = "longitude", precision = 11, scale = 8)
    private Double longitude;

    @DecimalMin(value = "0.0", message = "Altitude must be non-negative")
    @Column(name = "altitude_meters", precision = 7, scale = 2)
    private Double altitudeMeters;

    // Business logic methods
    @Transient
    public boolean isHighScoringVenue() {
        return averageFirstInningsScore != null && averageFirstInningsScore > 300;
    }

    @Transient
    public boolean isBattingFriendly() {
        return averageRunsPerWicket != null && averageRunsPerWicket > 35;
    }

    @Transient
    public boolean isBowlingFriendly() {
        return averageRunsPerWicket != null && averageRunsPerWicket < 25;
    }

    @Transient
    public boolean isBalancedVenue() {
        if (averageRunsPerWicket == null) return false;
        return averageRunsPerWicket >= 25 && averageRunsPerWicket <= 35;
    }

    @Transient
    public String getFullAddress() {
        return String.format("%s, %s, %s", name, city, country);
    }

    @Transient
    public boolean hasCoordinates() {
        return latitude != null && longitude != null;
    }

    @Transient
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               city != null && !city.trim().isEmpty() &&
               country != null && !country.trim().isEmpty();
    }

    /**
     * Update venue statistics after a match.
     */
    public void updateStatistics(int firstInningsScore, boolean successfulChase,
                               int runsPerWicket, String matchHighest, String matchLowest) {
        totalMatchesHosted++;

        // Update averages (simplified moving average)
        if (averageFirstInningsScore == null) {
            averageFirstInningsScore = (double) firstInningsScore;
        } else {
            averageFirstInningsScore = (averageFirstInningsScore + firstInningsScore) / 2;
        }

        if (averageChaseSuccessRate == null) {
            averageChaseSuccessRate = successfulChase ? 100.0 : 0.0;
        } else {
            double newRate = successfulChase ? 100.0 : 0.0;
            averageChaseSuccessRate = (averageChaseSuccessRate + newRate) / 2;
        }

        if (averageRunsPerWicket == null) {
            averageRunsPerWicket = (double) runsPerWicket;
        } else {
            averageRunsPerWicket = (averageRunsPerWicket + runsPerWicket) / 2;
        }

        // Update records if applicable
        updateRecords(matchHighest, matchLowest);
    }

    private void updateRecords(String matchHighest, String matchLowest) {
        // Simplified record updating logic
        // In a real implementation, this would parse and compare scores
        if (highestScore == null || matchHighest != null) {
            highestScore = matchHighest;
        }
        if (lowestScore == null || matchLowest != null) {
            lowestScore = matchLowest;
        }
    }
}
