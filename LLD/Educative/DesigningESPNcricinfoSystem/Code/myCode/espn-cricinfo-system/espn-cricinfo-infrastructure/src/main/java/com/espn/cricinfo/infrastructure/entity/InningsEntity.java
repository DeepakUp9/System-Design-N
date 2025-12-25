package com.espn.cricinfo.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity representing an innings in cricket.
 * Contains innings statistics and relationships.
 */
@Entity
@Table(name = "innings", indexes = {
    @Index(name = "idx_innings_match", columnList = "match_id"),
    @Index(name = "idx_innings_number", columnList = "innings_number"),
    @Index(name = "idx_innings_batting_team", columnList = "batting_team_id"),
    @Index(name = "idx_innings_active", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"match", "battingTeam", "bowlingTeam", "balls"})
@EqualsAndHashCode(callSuper = true, exclude = {"match", "battingTeam", "bowlingTeam", "balls"})
public class InningsEntity extends BaseEntity {

    @NotNull(message = "Innings number is required")
    @Column(name = "innings_number", nullable = false)
    private Integer inningsNumber;

    @NotNull(message = "Batting team is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batting_team_id", foreignKey = @ForeignKey(name = "fk_innings_batting_team"))
    private TeamEntity battingTeam;

    @NotNull(message = "Bowling team is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bowling_team_id", foreignKey = @ForeignKey(name = "fk_innings_bowling_team"))
    private TeamEntity bowlingTeam;

    @NotNull(message = "Match is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", foreignKey = @ForeignKey(name = "fk_innings_match"))
    private MatchEntity match;

    // Innings statistics
    @Column(name = "total_runs", nullable = false)
    @Builder.Default
    private Integer totalRuns = 0;

    @Column(name = "total_wickets", nullable = false)
    @Builder.Default
    private Integer totalWickets = 0;

    @Column(name = "total_overs", nullable = false)
    @Builder.Default
    private Integer totalOvers = 0;

    @Column(name = "total_balls", nullable = false)
    @Builder.Default
    private Integer totalBalls = 0;

    @Builder.Default
    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Builder.Default
    @Column(name = "is_declared", nullable = false)
    private Boolean isDeclared = false;

    // Innings result details
    @Column(name = "declared_overs")
    private Double declaredOvers;

    @Column(name = "declared_balls")
    private Integer declaredBalls;

    // Target for chase innings
    @Column(name = "target_runs")
    private Integer targetRuns;

    @Column(name = "target_overs")
    private Double targetOvers;

    @Column(name = "target_balls")
    private Integer targetBalls;

    // Relationships
    @OneToMany(mappedBy = "innings", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("overNumber ASC, ballNumber ASC")
    @Builder.Default
    private List<BallEntity> balls = new ArrayList<>();

    // Business logic methods
    @Transient
    public double getRunRate() {
        return totalOvers > 0 ? (double) totalRuns / totalOvers : 0.0;
    }

    @Transient
    public boolean isAllOut() {
        return totalWickets >= 10;
    }

    @Transient
    public boolean canContinue() {
        return !isCompleted && !isDeclared && !isAllOut();
    }

    @Transient
    public boolean isTargetAchieved() {
        return targetRuns != null && totalRuns >= targetRuns;
    }

    @Transient
    public Integer getRunsRemaining() {
        return targetRuns != null ? Math.max(0, targetRuns - totalRuns) : null;
    }

    @Transient
    public Integer getWicketsRemaining() {
        return Math.max(0, 10 - totalWickets);
    }

    @Transient
    public boolean isValid() {
        return inningsNumber != null && inningsNumber > 0 &&
               battingTeam != null &&
               bowlingTeam != null &&
               match != null &&
               !battingTeam.equals(bowlingTeam);
    }

    /**
     * Add a ball to the innings.
     */
    public void addBall(BallEntity ball) {
        if (balls == null) {
            balls = new ArrayList<>();
        }
        balls.add(ball);
        ball.setInnings(this);

        // Update innings statistics
        totalRuns += ball.getTotalRuns();
        totalBalls++;

        if (ball.isOverComplete()) {
            totalOvers++;
        }

        if (ball.isWicket()) {
            totalWickets++;
        }
    }

    /**
     * Complete the innings normally.
     */
    public void completeInnings() {
        isCompleted = true;
        // Could set end time or other completion logic here
    }

    /**
     * Declare the innings (Test cricket).
     */
    public void declareInnings(double overs, int balls) {
        isDeclared = true;
        isCompleted = true;
        declaredOvers = overs;
        declaredBalls = balls;
    }

    /**
     * Set target for chasing team.
     */
    public void setTarget(int runs, double overs, int balls) {
        targetRuns = runs;
        targetOvers = overs;
        targetBalls = balls;
    }

    /**
     * Check if this is a powerplay innings (first 10 overs in ODI/T20).
     */
    @Transient
    public boolean isPowerplay() {
        return totalOvers <= 10;
    }

    /**
     * Check if this is a death overs innings (last 5 overs).
     */
    @Transient
    public boolean isDeathOvers() {
        if (match == null || !match.isLimitedOvers()) return false;
        int maxOvers = match.getMaxOversPerInnings();
        return totalOvers >= (maxOvers - 5);
    }

    /**
     * Get required run rate for chase.
     */
    @Transient
    public double getRequiredRunRate() {
        if (targetRuns == null || targetOvers == null || targetOvers <= totalOvers) {
            return 0.0;
        }
        int remainingRuns = targetRuns - totalRuns;
        double remainingOvers = targetOvers - totalOvers;
        return remainingOvers > 0 ? (double) remainingRuns / remainingOvers : 0.0;
    }
}
