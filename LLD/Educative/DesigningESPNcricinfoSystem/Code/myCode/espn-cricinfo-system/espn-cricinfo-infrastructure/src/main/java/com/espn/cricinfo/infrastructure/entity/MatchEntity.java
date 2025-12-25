package com.espn.cricinfo.infrastructure.entity;

import com.espn.cricinfo.domain.enums.MatchFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity representing a cricket match.
 * Contains comprehensive match information and relationships.
 */
@Entity
@Table(name = "matches", indexes = {
    @Index(name = "idx_match_start_time", columnList = "start_time"),
    @Index(name = "idx_match_format", columnList = "format"),
    @Index(name = "idx_match_status", columnList = "status"),
    @Index(name = "idx_match_venue", columnList = "venue_id"),
    @Index(name = "idx_match_winner", columnList = "winner_team_id"),
    @Index(name = "idx_match_active", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"team1", "team2", "venue", "winner", "innings", "balls"})
@EqualsAndHashCode(callSuper = true, exclude = {"team1", "team2", "venue", "winner", "innings", "balls"})
public class MatchEntity extends BaseEntity {

    @NotNull(message = "Match name is required")
    @Column(name = "match_name", nullable = false, length = 200)
    private String matchName;

    @NotNull(message = "Match format is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false, length = 20)
    private MatchFormat format;

    @NotNull(message = "Team 1 is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team1_id", foreignKey = @ForeignKey(name = "fk_match_team1"))
    private TeamEntity team1;

    @NotNull(message = "Team 2 is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team2_id", foreignKey = @ForeignKey(name = "fk_match_team2"))
    private TeamEntity team2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_team_id", foreignKey = @ForeignKey(name = "fk_match_winner"))
    private TeamEntity winner;

    @NotNull(message = "Match status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private MatchStatus status = MatchStatus.NOT_STARTED;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @NotNull(message = "Venue is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id", foreignKey = @ForeignKey(name = "fk_match_venue"))
    private VenueEntity venue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", foreignKey = @ForeignKey(name = "fk_match_tournament"))
    private TournamentEntity tournament;

    // Match progress fields
    @Column(name = "current_innings_number")
    @Builder.Default
    private Integer currentInningsNumber = 0;

    @Builder.Default
    @Column(name = "is_first_innings")
    private Boolean isFirstInnings = false;

    @Builder.Default
    @Column(name = "is_second_innings")
    private Boolean isSecondInnings = false;

    // Match statistics
    @Column(name = "total_overs")
    @Builder.Default
    private Integer totalOvers = 0;

    @Column(name = "total_balls")
    @Builder.Default
    private Integer totalBalls = 0;

    // Match result details
    @Column(name = "result_type", length = 50)
    private String resultType; // NORMAL, TIE, NO_RESULT, etc.

    @Column(name = "result_description", length = 500)
    private String resultDescription;

    @Column(name = "man_of_the_match_player_id")
    private Long manOfTheMatchPlayerId;

    // Weather conditions
    @Column(name = "weather_condition", length = 50)
    private String weatherCondition; // SUNNY, CLOUDY, RAINY, etc.

    @Column(name = "temperature_celsius", precision = 4, scale = 1)
    private Double temperatureCelsius;

    @Column(name = "humidity_percentage", precision = 5, scale = 2)
    private Double humidityPercentage;

    @Column(name = "wind_speed_kmh", precision = 5, scale = 2)
    private Double windSpeedKmh;

    // Umpires and officials
    @Column(name = "umpire1_name", length = 100)
    private String umpire1Name;

    @Column(name = "umpire2_name", length = 100)
    private String umpire2Name;

    @Column(name = "third_umpire_name", length = 100)
    private String thirdUmpireName;

    @Column(name = "match_referee_name", length = 100)
    private String matchRefereeName;

    // Relationships
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("inningsNumber ASC")
    @Builder.Default
    private List<InningsEntity> innings = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("timestamp ASC")
    @Builder.Default
    private List<BallEntity> balls = new ArrayList<>();

    // Current innings (transient for convenience)
    @Transient
    public InningsEntity getCurrentInnings() {
        if (innings == null || innings.isEmpty() || currentInningsNumber == null || currentInningsNumber == 0) {
            return null;
        }
        return innings.stream()
                .filter(i -> i.getInningsNumber().equals(currentInningsNumber))
                .findFirst()
                .orElse(null);
    }

    // Enums
    public enum MatchStatus {
        NOT_STARTED,
        IN_PROGRESS,
        PAUSED,
        COMPLETED,
        ABANDONED
    }

    // Business logic methods
    @Transient
    public boolean isLimitedOvers() {
        return format != null && format.isLimitedOvers();
    }

    @Transient
    public int getMaxOversPerInnings() {
        return format != null ? format.getOversPerInnings() : 0;
    }

    @Transient
    public int getMaxInnings() {
        return format != null ? format.getMaxInnings() : 2;
    }

    @Transient
    public boolean canDeclareInnings() {
        return format != null && format.canDeclareInnings();
    }

    @Transient
    public boolean isMatchComplete() {
        return status == MatchStatus.COMPLETED || status == MatchStatus.ABANDONED;
    }

    @Transient
    public boolean isActive() {
        return status == MatchStatus.IN_PROGRESS || status == MatchStatus.PAUSED;
    }

    @Transient
    public boolean canStartNewInnings() {
        if (!isActive()) return false;

        boolean hasCompletedInnings = currentInningsNumber < getMaxInnings();
        boolean currentInningsFinished = getCurrentInnings() == null ||
                                       getCurrentInnings().isCompleted() ||
                                       getCurrentInnings().isDeclared();

        return hasCompletedInnings && currentInningsFinished;
    }

    @Transient
    public boolean isValid() {
        return matchName != null && !matchName.trim().isEmpty() &&
               format != null &&
               team1 != null &&
               team2 != null &&
               venue != null &&
               !team1.equals(team2); // Teams must be different
    }

    @Transient
    public String getDisplayName() {
        return String.format("%s vs %s", team1.getShortName(), team2.getShortName());
    }

    @Transient
    public String getFullDescription() {
        return String.format("%s: %s vs %s at %s (%s)",
                format.getDisplayName(),
                team1.getName(),
                team2.getName(),
                venue.getName(),
                venue.getCity());
    }

    /**
     * Add an innings to the match.
     */
    public void addInnings(InningsEntity inningsEntity) {
        if (innings == null) {
            innings = new ArrayList<>();
        }
        innings.add(inningsEntity);
        inningsEntity.setMatch(this);
        currentInningsNumber = inningsEntity.getInningsNumber();
        updateInningsFlags();
    }

    /**
     * Add a ball to the match.
     */
    public void addBall(BallEntity ball) {
        if (balls == null) {
            balls = new ArrayList<>();
        }
        balls.add(ball);
        ball.setMatch(this);
        totalBalls++;
    }

    /**
     * Update innings flags based on current innings number.
     */
    private void updateInningsFlags() {
        isFirstInnings = currentInningsNumber == 1;
        isSecondInnings = currentInningsNumber == 2;
    }

    /**
     * Set the match winner.
     */
    public void setWinner(TeamEntity winner) {
        this.winner = winner;
        if (winner != null) {
            resultType = winner.equals(team1) ? "TEAM1_WINS" : "TEAM2_WINS";
        } else {
            resultType = "DRAW_OR_TIE";
        }
    }

    /**
     * Mark match as completed.
     */
    public void completeMatch() {
        status = MatchStatus.COMPLETED;
        endTime = LocalDateTime.now();
    }

    /**
     * Mark match as abandoned.
     */
    public void abandonMatch() {
        status = MatchStatus.ABANDONED;
        endTime = LocalDateTime.now();
        resultType = "ABANDONED";
    }
}
