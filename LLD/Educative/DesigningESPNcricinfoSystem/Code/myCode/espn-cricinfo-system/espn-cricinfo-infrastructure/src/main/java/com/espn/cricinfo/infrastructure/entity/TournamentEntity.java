package com.espn.cricinfo.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity representing a cricket tournament.
 * Contains tournament information, participating teams, and match schedule.
 */
@Entity
@Table(name = "tournaments", indexes = {
    @Index(name = "idx_tournament_name", columnList = "name"),
    @Index(name = "idx_tournament_start_date", columnList = "start_date"),
    @Index(name = "idx_tournament_status", columnList = "status"),
    @Index(name = "idx_tournament_active", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"organizingBody", "teams", "matches", "venues"})
@EqualsAndHashCode(callSuper = true, exclude = {"organizingBody", "teams", "matches", "venues"})
public class TournamentEntity extends BaseEntity {

    @NotBlank(message = "Tournament name is required")
    @Size(min = 2, max = 200, message = "Tournament name must be between 2 and 200 characters")
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Size(max = 20, message = "Short name cannot exceed 20 characters")
    @Column(name = "short_name", length = 20)
    private String shortName;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull(message = "Tournament format is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false, length = 20)
    private TournamentFormat format;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull(message = "Tournament status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private TournamentStatus status = TournamentStatus.UPCOMING;

    @Size(max = 100, message = "Host country cannot exceed 100 characters")
    @Column(name = "host_country", length = 100)
    private String hostCountry;

    @Size(max = 200, message = "Host cities cannot exceed 200 characters")
    @Column(name = "host_cities", length = 200)
    private String hostCities;

    // Tournament details
    @Column(name = "total_teams")
    private Integer totalTeams;

    @Column(name = "total_matches")
    private Integer totalMatches;

    @Column(name = "matches_completed", nullable = false)
    @Builder.Default
    private Integer matchesCompleted = 0;

    @Size(max = 100, message = "Organizing body cannot exceed 100 characters")
    @Column(name = "organizing_body", length = 100)
    private String organizingBody;

    @Size(max = 100, message = "Sponsor cannot exceed 100 characters")
    @Column(name = "sponsor", length = 100)
    private String sponsor;

    @DecimalMin(value = "0.0", message = "Prize money must be non-negative")
    @Column(name = "prize_money_million", precision = 10, scale = 2)
    private Double prizeMoneyMillion;

    // Winner information
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_team_id", foreignKey = @ForeignKey(name = "fk_tournament_winner"))
    private TeamEntity winner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "runner_up_team_id", foreignKey = @ForeignKey(name = "fk_tournament_runner_up"))
    private TeamEntity runnerUp;

    @Size(max = 100, message = "Player of tournament cannot exceed 100 characters")
    @Column(name = "player_of_tournament", length = 100)
    private String playerOfTournament;

    // Relationships
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tournament_teams",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @Builder.Default
    private List<TeamEntity> teams = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("startTime ASC")
    @Builder.Default
    private List<MatchEntity> matches = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tournament_venues",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "venue_id")
    )
    @Builder.Default
    private List<VenueEntity> venues = new ArrayList<>();

    // Enums
    public enum TournamentFormat {
        WORLD_CUP,
        CHAMPIONS_TROPHY,
        IPL,
        PSL,
        BBL,
        TEST_SERIES,
        ODI_SERIES,
        T20_SERIES,
        LEAGUE,
        KNOCKOUT,
        ROUND_ROBIN
    }

    public enum TournamentStatus {
        UPCOMING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    // Business logic methods
    @Transient
    public boolean isActive() {
        return status == TournamentStatus.IN_PROGRESS;
    }

    @Transient
    public boolean isCompleted() {
        return status == TournamentStatus.COMPLETED;
    }

    @Transient
    public double getCompletionPercentage() {
        if (totalMatches == null || totalMatches == 0) return 0.0;
        return (double) matchesCompleted / totalMatches * 100;
    }

    @Transient
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               format != null &&
               startDate != null &&
               (endDate == null || !endDate.isBefore(startDate));
    }

    @Transient
    public LocalDateTime getStartDateTime() {
        return startDate != null ? startDate.atStartOfDay() : null;
    }

    @Transient
    public LocalDateTime getEndDateTime() {
        return endDate != null ? endDate.atTime(23, 59, 59) : null;
    }

    /**
     * Add a team to the tournament.
     */
    public void addTeam(TeamEntity team) {
        if (teams == null) {
            teams = new ArrayList<>();
        }
        if (!teams.contains(team)) {
            teams.add(team);
            if (totalTeams == null) {
                totalTeams = 0;
            }
            totalTeams++;
        }
    }

    /**
     * Remove a team from the tournament.
     */
    public void removeTeam(TeamEntity team) {
        if (teams != null) {
            teams.remove(team);
            if (totalTeams != null && totalTeams > 0) {
                totalTeams--;
            }
        }
    }

    /**
     * Add a match to the tournament.
     */
    public void addMatch(MatchEntity match) {
        if (matches == null) {
            matches = new ArrayList<>();
        }
        matches.add(match);
        match.setTournament(this);
        if (totalMatches == null) {
            totalMatches = 0;
        }
        totalMatches++;
    }

    /**
     * Add a venue to the tournament.
     */
    public void addVenue(VenueEntity venue) {
        if (venues == null) {
            venues = new ArrayList<>();
        }
        if (!venues.contains(venue)) {
            venues.add(venue);
        }
    }

    /**
     * Update tournament progress.
     */
    public void updateProgress(int completedMatches) {
        this.matchesCompleted = completedMatches;
        if (completedMatches >= totalMatches) {
            this.status = TournamentStatus.COMPLETED;
        }
    }

    /**
     * Set tournament winner.
     */
    public void setWinner(TeamEntity winner, TeamEntity runnerUp) {
        this.winner = winner;
        this.runnerUp = runnerUp;
        this.status = TournamentStatus.COMPLETED;
    }

    /**
     * Cancel the tournament.
     */
    public void cancel() {
        this.status = TournamentStatus.CANCELLED;
    }
}
