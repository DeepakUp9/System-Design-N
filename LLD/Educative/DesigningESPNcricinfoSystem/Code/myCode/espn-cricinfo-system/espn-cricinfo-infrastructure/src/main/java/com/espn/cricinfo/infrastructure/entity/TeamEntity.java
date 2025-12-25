package com.espn.cricinfo.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity representing a cricket team.
 * Includes team information, players, and performance statistics.
 */
@Entity
@Table(name = "teams", indexes = {
    @Index(name = "idx_team_name", columnList = "name"),
    @Index(name = "idx_team_country", columnList = "country"),
    @Index(name = "idx_team_active", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"players", "captain", "wicketKeeper"})
@EqualsAndHashCode(callSuper = true, exclude = {"players", "captain", "wicketKeeper"})
public class TeamEntity extends BaseEntity {

    @NotBlank(message = "Team name is required")
    @Size(min = 2, max = 100, message = "Team name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @NotBlank(message = "Team short name is required")
    @Size(min = 2, max = 10, message = "Team short name must be between 2 and 10 characters")
    @Column(name = "short_name", nullable = false, unique = true, length = 10)
    private String shortName;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters")
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Size(max = 255, message = "Home ground cannot exceed 255 characters")
    @Column(name = "home_ground", length = 255)
    private String homeGround;

    @Size(max = 500, message = "Logo URL cannot exceed 500 characters")
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Size(max = 7, message = "Primary color must be a valid hex color")
    @Column(name = "primary_color", length = 7)
    private String primaryColor;

    @Size(max = 7, message = "Secondary color must be a valid hex color")
    @Column(name = "secondary_color", length = 7)
    private String secondaryColor;

    // Team Statistics
    @Column(name = "total_matches_played", nullable = false)
    @Builder.Default
    private Integer totalMatchesPlayed = 0;

    @Column(name = "total_matches_won", nullable = false)
    @Builder.Default
    private Integer totalMatchesWon = 0;

    @Column(name = "total_matches_lost", nullable = false)
    @Builder.Default
    private Integer totalMatchesLost = 0;

    @Column(name = "total_matches_drawn", nullable = false)
    @Builder.Default
    private Integer totalMatchesDrawn = 0;

    // Relationships
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PlayerEntity> players = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captain_id", foreignKey = @ForeignKey(name = "fk_team_captain"))
    private PlayerEntity captain;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wicket_keeper_id", foreignKey = @ForeignKey(name = "fk_team_wicket_keeper"))
    private PlayerEntity wicketKeeper;

    // Calculated fields
    @Transient
    public double getWinPercentage() {
        if (totalMatchesPlayed == 0) return 0.0;
        return (double) totalMatchesWon / totalMatchesPlayed * 100;
    }

    @Transient
    public boolean hasPlayer(PlayerEntity player) {
        return players != null && players.contains(player);
    }

    @Transient
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               shortName != null && !shortName.trim().isEmpty() &&
               country != null && !country.trim().isEmpty();
    }

    /**
     * Update team statistics after a match.
     */
    public void updateStatistics(boolean won, boolean lost, boolean drawn) {
        totalMatchesPlayed++;
        if (won) totalMatchesWon++;
        if (lost) totalMatchesLost++;
        if (drawn) totalMatchesDrawn++;
    }

    /**
     * Add a player to the team.
     */
    public void addPlayer(PlayerEntity player) {
        if (players == null) {
            players = new ArrayList<>();
        }
        if (!players.contains(player)) {
            players.add(player);
            player.setTeam(this);
        }
    }

    /**
     * Remove a player from the team.
     */
    public void removePlayer(PlayerEntity player) {
        if (players != null) {
            players.remove(player);
            player.setTeam(null);
        }
    }

    /**
     * Set captain and ensure the player is in the team.
     */
    public void setCaptain(PlayerEntity captain) {
        if (captain != null && !hasPlayer(captain)) {
            throw new IllegalArgumentException("Captain must be a player in the team");
        }
        this.captain = captain;
    }

    /**
     * Set wicket keeper and ensure the player is in the team and is a wicket keeper.
     */
    public void setWicketKeeper(PlayerEntity wicketKeeper) {
        if (wicketKeeper != null) {
            if (!hasPlayer(wicketKeeper)) {
                throw new IllegalArgumentException("Wicket keeper must be a player in the team");
            }
            if (!"WICKET_KEEPER".equals(wicketKeeper.getRole())) {
                throw new IllegalArgumentException("Player must be a wicket keeper");
            }
        }
        this.wicketKeeper = wicketKeeper;
    }
}
