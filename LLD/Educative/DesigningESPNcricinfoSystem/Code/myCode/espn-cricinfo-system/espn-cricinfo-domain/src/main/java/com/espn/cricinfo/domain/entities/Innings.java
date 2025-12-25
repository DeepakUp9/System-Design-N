package com.espn.cricinfo.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Entity representing an innings in cricket.
 */
@Data
@Builder
public class Innings {
    private Long id;
    private Team battingTeam;
    private Team bowlingTeam;
    private int inningsNumber;
    private int totalRuns;
    private int totalWickets;
    private int totalOvers;
    private int totalBalls;
    private boolean isCompleted;
    private boolean isDeclared;
    private List<Ball> balls;

    public double getRunRate() {
        return totalOvers > 0 ? (double) totalRuns / totalOvers : 0.0;
    }

    public boolean isAllOut() {
        return totalWickets == 10;
    }

    public boolean canContinue() {
        return !isCompleted && !isDeclared && !isAllOut();
    }
}