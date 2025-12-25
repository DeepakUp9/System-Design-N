package com.espn.cricinfo.domain.entities;

import com.espn.cricinfo.domain.enums.MatchFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a cricket match.
 * Used by match format strategies to determine valid operations.
 */
@Data
@Builder
public class Match {
    private Long id;
    private String matchName;
    private MatchFormat format;
    private Team team1;
    private Team team2;
    private Team winner;
    private MatchStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Venue venue;

    // Current match state
    private Innings currentInnings;
    private int currentInningsNumber;
    private boolean isFirstInnings;
    private boolean isSecondInnings;

    // Match statistics
    private int totalOvers;
    private int totalBalls;
    private List<Innings> innings;

    public boolean isLimitedOvers() {
        return format.isLimitedOvers();
    }

    public int getMaxOversPerInnings() {
        return format.getOversPerInnings();
    }

    public int getMaxInnings() {
        return format.getMaxInnings();
    }

    public boolean canDeclareInnings() {
        return format.canDeclareInnings();
    }

    public boolean isMatchComplete() {
        return status == MatchStatus.COMPLETED || status == MatchStatus.ABANDONED;
    }

    public boolean canStartNewInnings() {
        if (isLimitedOvers()) {
            return currentInningsNumber < getMaxInnings() &&
                   (currentInnings == null || currentInnings.isCompleted());
        } else {
            // Test cricket rules
            return currentInningsNumber < getMaxInnings() &&
                   (currentInnings == null || currentInnings.isCompleted() || currentInnings.isDeclared());
        }
    }

    public enum MatchStatus {
        NOT_STARTED,
        IN_PROGRESS,
        PAUSED,
        COMPLETED,
        ABANDONED
    }
}