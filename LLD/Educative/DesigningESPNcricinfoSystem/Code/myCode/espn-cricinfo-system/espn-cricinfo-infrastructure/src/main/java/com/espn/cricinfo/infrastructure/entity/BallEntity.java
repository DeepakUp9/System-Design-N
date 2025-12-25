package com.espn.cricinfo.infrastructure.entity;

import com.espn.cricinfo.domain.enums.BallType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * JPA entity representing a single ball bowled in cricket.
 * Contains comprehensive ball-by-ball data for analytics.
 */
@Entity
@Table(name = "balls", indexes = {
    @Index(name = "idx_ball_match", columnList = "match_id"),
    @Index(name = "idx_ball_innings", columnList = "innings_id"),
    @Index(name = "idx_ball_over", columnList = "over_number"),
    @Index(name = "idx_ball_bowler", columnList = "bowler_name"),
    @Index(name = "idx_ball_batsman", columnList = "batsman_name"),
    @Index(name = "idx_ball_timestamp", columnList = "timestamp"),
    @Index(name = "idx_ball_active", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"match", "innings"})
@EqualsAndHashCode(callSuper = true, exclude = {"match", "innings"})
public class BallEntity extends BaseEntity {

    @NotNull(message = "Match is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", foreignKey = @ForeignKey(name = "fk_ball_match"))
    private MatchEntity match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "innings_id", foreignKey = @ForeignKey(name = "fk_ball_innings"))
    private InningsEntity innings;

    @NotNull(message = "Over number is required")
    @Min(value = 0, message = "Over number must be non-negative")
    @Column(name = "over_number", nullable = false)
    private Integer overNumber;

    @NotNull(message = "Ball number is required")
    @Min(value = 1, message = "Ball number must be at least 1")
    @Max(value = 6, message = "Ball number must not exceed 6")
    @Column(name = "ball_number", nullable = false)
    private Integer ballNumber;

    @NotNull(message = "Ball type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "ball_type", nullable = false, length = 20)
    private BallType ballType;

    @NotNull(message = "Runs scored is required")
    @Min(value = 0, message = "Runs scored must be non-negative")
    @Max(value = 6, message = "Runs scored must not exceed 6 for legal deliveries")
    @Column(name = "runs_scored", nullable = false)
    private Integer runsScored;

    @Builder.Default
    @Column(name = "is_wicket", nullable = false)
    private Boolean isWicket = false;

    @Builder.Default
    @Column(name = "is_boundary", nullable = false)
    private Boolean isBoundary = false;

    @Builder.Default
    @Column(name = "is_six", nullable = false)
    private Boolean isSix = false;

    @NotBlank(message = "Bowler name is required")
    @Size(max = 100, message = "Bowler name cannot exceed 100 characters")
    @Column(name = "bowler_name", nullable = false, length = 100)
    private String bowlerName;

    @NotBlank(message = "Batsman name is required")
    @Size(max = 100, message = "Batsman name cannot exceed 100 characters")
    @Column(name = "batsman_name", nullable = false, length = 100)
    private String batsmanName;

    @NotNull(message = "Timestamp is required")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    // Advanced analytics fields
    @DecimalMin(value = "80.0", message = "Ball speed must be at least 80 km/h")
    @DecimalMax(value = "170.0", message = "Ball speed must not exceed 170 km/h")
    @Column(name = "ball_speed_kmh", precision = 5, scale = 2)
    private Double ballSpeedKmh;

    @Size(max = 50, message = "Pitch location cannot exceed 50 characters")
    @Column(name = "pitch_location", length = 50)
    private String pitchLocation; // e.g., "off stump", "leg stump", "middle"

    @Size(max = 50, message = "Shot played cannot exceed 50 characters")
    @Column(name = "shot_played", length = 50)
    private String shotPlayed; // e.g., "cover drive", "pull shot", "defensive"

    // Umpire decisions
    @Size(max = 20, message = "Umpire decision cannot exceed 20 characters")
    @Column(name = "umpire_decision", length = 20)
    private String umpireDecision; // NOT_OUT, OUT, LBW, CAUGHT, etc.

    @Column(name = "reviewed", nullable = false)
    @Builder.Default
    private Boolean reviewed = false;

    @Size(max = 50, message = "Review result cannot exceed 50 characters")
    @Column(name = "review_result", length = 50)
    private String reviewResult; // UMPIRE_CALL, OVERRULED, etc.

    // Commentary and notes
    @Size(max = 500, message = "Commentary cannot exceed 500 characters")
    @Column(name = "commentary", length = 500)
    private String commentary;

    @Size(max = 200, message = "Notes cannot exceed 200 characters")
    @Column(name = "notes", length = 200)
    private String notes;

    // Business logic methods
    @Transient
    public boolean isLegalDelivery() {
        return ballType == BallType.LEGAL_DELIVERY;
    }

    @Transient
    public boolean isExtra() {
        return ballType.isExtra();
    }

    @Transient
    public int getTotalRuns() {
        int extraRuns = switch (ballType) {
            case NO_BALL, WIDE -> 1;
            default -> 0;
        };
        return runsScored + extraRuns;
    }

    @Transient
    public boolean isOverComplete() {
        return ballNumber == 6 && isLegalDelivery();
    }

    @Transient
    public String getBallDescription() {
        return String.format("Over %d.%d: %s to %s - %d runs",
                overNumber, ballNumber, bowlerName, batsmanName, runsScored);
    }

    @Transient
    public String getOverBall() {
        return String.format("%d.%d", overNumber, ballNumber);
    }

    @Transient
    public boolean isDotBall() {
        return runsScored == 0 && !isWicket && isLegalDelivery();
    }

    @Transient
    public boolean isMaidenOver() {
        // This would need to check the entire over - simplified logic
        return runsScored == 0 && !isWicket && isLegalDelivery();
    }

    @Transient
    public boolean isPowerplayBall() {
        return overNumber < 10; // First 10 overs are powerplay in most formats
    }

    @Transient
    public boolean isDeathOverBall() {
        if (match == null || !match.isLimitedOvers()) return false;
        int maxOvers = match.getMaxOversPerInnings();
        return overNumber >= (maxOvers - 5); // Last 5 overs
    }

    @Transient
    public boolean isValid() {
        return match != null &&
               overNumber != null && overNumber >= 0 &&
               ballNumber != null && ballNumber >= 1 && ballNumber <= 6 &&
               ballType != null &&
               runsScored != null && runsScored >= 0 &&
               bowlerName != null && !bowlerName.trim().isEmpty() &&
               batsmanName != null && !batsmanName.trim().isEmpty() &&
               timestamp != null;
    }

    /**
     * Set ball properties based on runs and wicket.
     */
    public void setBallResult(int runs, boolean wicket, BallType type) {
        this.runsScored = runs;
        this.isWicket = wicket;
        this.ballType = type;
        this.isBoundary = runs == 4;
        this.isSix = runs == 6;
    }

    /**
     * Record umpire decision.
     */
    public void recordUmpireDecision(String decision) {
        this.umpireDecision = decision;
    }

    /**
     * Record DRS review result.
     */
    public void recordReviewResult(String result) {
        this.reviewResult = result;
        this.reviewed = true;
    }

    /**
     * Add commentary for the ball.
     */
    public void addCommentary(String commentary) {
        this.commentary = commentary;
    }
}
