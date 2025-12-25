package com.espn.cricinfo.infrastructure.entity;

import com.espn.cricinfo.domain.enums.MatchFormat;
import com.espn.cricinfo.domain.valueobjects.PlayerStats;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * JPA entity representing a cricket player.
 * Contains comprehensive player information and performance statistics.
 */
@Entity
@Table(name = "players", indexes = {
    @Index(name = "idx_player_name", columnList = "full_name"),
    @Index(name = "idx_player_team", columnList = "team_id"),
    @Index(name = "idx_player_role", columnList = "role"),
    @Index(name = "idx_player_active", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"team", "statsByFormat"})
@EqualsAndHashCode(callSuper = true, exclude = {"team", "statsByFormat"})
public class PlayerEntity extends BaseEntity {

    @NotBlank(message = "Player name is required")
    @Size(min = 2, max = 100, message = "Player name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 150, message = "Full name cannot exceed 150 characters")
    @Column(name = "full_name", length = 150)
    private String fullName;

    @NotBlank(message = "Player role is required")
    @Pattern(regexp = "^(BATSMAN|BOWLER|ALL_ROUNDER|WICKET_KEEPER)$",
             message = "Role must be BATSMAN, BOWLER, ALL_ROUNDER, or WICKET_KEEPER")
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @Size(max = 50, message = "Nationality cannot exceed 50 characters")
    @Column(name = "nationality", length = 50)
    private String nationality;

    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Min(value = 15, message = "Age must be at least 15")
    @Max(value = 50, message = "Age must not exceed 50")
    @Column(name = "age")
    private Integer age;

    @Size(max = 10, message = "Jersey number cannot exceed 10 characters")
    @Column(name = "jersey_number", length = 10)
    private String jerseyNumber;

    @Size(max = 500, message = "Profile picture URL cannot exceed 500 characters")
    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @Size(max = 1000, message = "Biography cannot exceed 1000 characters")
    @Column(name = "biography", length = 1000)
    private String biography;

    // Physical attributes
    @DecimalMin(value = "1.0", message = "Height must be at least 1.0 meters")
    @DecimalMax(value = "2.5", message = "Height must not exceed 2.5 meters")
    @Column(name = "height_meters", precision = 3, scale = 2)
    private Double heightMeters;

    @DecimalMin(value = "30.0", message = "Weight must be at least 30.0 kg")
    @DecimalMax(value = "150.0", message = "Weight must not exceed 150.0 kg")
    @Column(name = "weight_kg", precision = 5, scale = 2)
    private Double weightKg;

    // Batting style
    @Pattern(regexp = "^(RIGHT_HANDED|LEFT_HANDED)$",
             message = "Batting style must be RIGHT_HANDED or LEFT_HANDED")
    @Column(name = "batting_style", length = 15)
    private String battingStyle;

    // Bowling style
    @Pattern(regexp = "^(RIGHT_ARM_FAST|RIGHT_ARM_MEDIUM|RIGHT_ARM_SPIN|LEFT_ARM_FAST|LEFT_ARM_MEDIUM|LEFT_ARM_SPIN|NONE)$",
             message = "Bowling style must be a valid style or NONE")
    @Column(name = "bowling_style", length = 20)
    private String bowlingStyle;

    // Status
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "retirement_date")
    private LocalDate retirementDate;

    // Career statistics (JSON stored)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "career_stats", columnDefinition = "jsonb")
    private PlayerStats careerStats;

    // Statistics by format (JSON stored)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "stats_by_format", columnDefinition = "jsonb")
    @Builder.Default
    private Map<MatchFormat, PlayerStats> statsByFormat = new HashMap<>();

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "fk_player_team"))
    private TeamEntity team;

    // Calculated fields and business logic
    @Transient
    public boolean isWicketKeeper() {
        return "WICKET_KEEPER".equals(role);
    }

    @Transient
    public boolean isBatsman() {
        return "BATSMAN".equals(role) || "ALL_ROUNDER".equals(role);
    }

    @Transient
    public boolean isBowler() {
        return "BOWLER".equals(role) || "ALL_ROUNDER".equals(role);
    }

    @Transient
    public boolean isRetired() {
        return retirementDate != null && retirementDate.isBefore(LocalDate.now());
    }

    @Transient
    public boolean isAvailable() {
        return isActive && !isRetired();
    }

    @Transient
    public double getBattingAverage() {
        if (careerStats == null) return 0.0;
        return careerStats.getBattingAverage();
    }

    @Transient
    public double getBowlingAverage() {
        if (careerStats == null) return 0.0;
        return careerStats.getBowlingAverage();
    }

    @Transient
    public PlayerStats getStatsForFormat(MatchFormat format) {
        return statsByFormat.getOrDefault(format, PlayerStats.builder().build());
    }

    /**
     * Update player statistics for a specific format.
     */
    public void updateStatsForFormat(MatchFormat format, PlayerStats newStats) {
        statsByFormat.put(format, newStats);
        // Could update career stats here based on format stats
        updateCareerStats();
    }

    /**
     * Update career statistics based on all format statistics.
     */
    private void updateCareerStats() {
        // Aggregate statistics from all formats
        // This is a simplified implementation
        if (careerStats == null) {
            careerStats = PlayerStats.builder().build();
        }
        // In a real implementation, this would aggregate all format stats
    }

    /**
     * Retire the player.
     */
    public void retire() {
        this.retirementDate = LocalDate.now();
        this.isActive = false;
    }

    /**
     * Reactivate the player.
     */
    public void reactivate() {
        this.retirementDate = null;
        this.isActive = true;
    }

    /**
     * Validate player data.
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               role != null && !role.trim().isEmpty() &&
               isValidRole() &&
               (age == null || (age >= 15 && age <= 50));
    }

    private boolean isValidRole() {
        return "BATSMAN".equals(role) ||
               "BOWLER".equals(role) ||
               "ALL_ROUNDER".equals(role) ||
               "WICKET_KEEPER".equals(role);
    }
}
