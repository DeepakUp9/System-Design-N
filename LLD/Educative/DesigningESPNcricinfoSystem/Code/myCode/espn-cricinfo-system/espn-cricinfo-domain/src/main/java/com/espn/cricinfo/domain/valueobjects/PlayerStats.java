package com.espn.cricinfo.domain.valueobjects;

import lombok.Builder;
import lombok.Value;

/**
 * Immutable value object representing player statistics.
 * Contains comprehensive batting and bowling statistics.
 */
@Value
@Builder
public class PlayerStats {

    // Batting statistics
    @Builder.Default
    int totalRuns = 0;
    @Builder.Default
    int totalBallsFaced = 0;
    @Builder.Default
    int totalInnings = 0;
    @Builder.Default
    int totalNotOuts = 0;
    @Builder.Default
    double battingAverage = 0.0;
    @Builder.Default
    double battingStrikeRate = 0.0;
    @Builder.Default
    int hundreds = 0;
    @Builder.Default
    int fifties = 0;
    @Builder.Default
    int highestScore = 0;

    // Bowling statistics
    @Builder.Default
    int totalWickets = 0;
    @Builder.Default
    int totalOversBowled = 0;
    @Builder.Default
    int totalRunsConceded = 0;
    @Builder.Default
    double bowlingAverage = 0.0;
    @Builder.Default
    double bowlingEconomy = 0.0;
    @Builder.Default
    double bowlingStrikeRate = 0.0;
    @Builder.Default
    int fiveWicketHauls = 0;
    @Builder.Default
    int tenWicketHauls = 0;
    @Builder.Default
    int bestBowlingFigures = 0;

    // Additional statistics
    @Builder.Default
    int totalMatches = 0;
    @Builder.Default
    int totalCatches = 0;
    @Builder.Default
    int totalRunOuts = 0;
    @Builder.Default
    int totalStumpings = 0;

    /**
     * Calculate batting average dynamically.
     */
    public double getBattingAverage() {
        int inningsWithDismissals = totalInnings - totalNotOuts;
        return inningsWithDismissals > 0 ? (double) totalRuns / inningsWithDismissals : 0.0;
    }

    /**
     * Calculate batting strike rate dynamically.
     */
    public double getBattingStrikeRate() {
        return totalBallsFaced > 0 ? (totalRuns * 100.0) / totalBallsFaced : 0.0;
    }

    /**
     * Calculate bowling average dynamically.
     */
    public double getBowlingAverage() {
        return totalWickets > 0 ? (double) totalRunsConceded / totalWickets : 0.0;
    }

    /**
     * Calculate bowling economy dynamically.
     */
    public double getBowlingEconomy() {
        return totalOversBowled > 0 ? (double) totalRunsConceded / totalOversBowled : 0.0;
    }

    /**
     * Calculate bowling strike rate dynamically.
     */
    public double getBowlingStrikeRate() {
        return totalWickets > 0 ? (double) (totalOversBowled * 6) / totalWickets : 0.0;
    }

    /**
     * Check if player has significant batting performance.
     */
    public boolean isQualityBatsman() {
        return getBattingAverage() >= 35.0 && getBattingStrikeRate() >= 120.0;
    }

    /**
     * Check if player has significant bowling performance.
     */
    public boolean isQualityBowler() {
        return getBowlingAverage() <= 30.0 && getBowlingEconomy() <= 5.0;
    }

    /**
     * Check if player is an all-rounder.
     */
    public boolean isAllRounder() {
        return isQualityBatsman() && totalWickets >= 50;
    }

    /**
     * Get batting class based on runs scored.
     */
    public String getBattingClass() {
        if (totalRuns >= 10000) return "LEGEND";
        if (totalRuns >= 5000) return "GREAT";
        if (totalRuns >= 2000) return "GOOD";
        if (totalRuns >= 1000) return "MODERATE";
        return "BEGINNER";
    }

    /**
     * Get bowling class based on wickets taken.
     */
    public String getBowlingClass() {
        if (totalWickets >= 400) return "LEGEND";
        if (totalWickets >= 200) return "GREAT";
        if (totalWickets >= 100) return "GOOD";
        if (totalWickets >= 50) return "MODERATE";
        return "BEGINNER";
    }

    /**
     * Create empty stats for new players.
     */
    public static PlayerStats empty() {
        return PlayerStats.builder().build();
    }

    /**
     * Add batting performance to existing stats.
     */
    public PlayerStats addBattingInnings(int runs, boolean notOut, int ballsFaced) {
        return PlayerStats.builder()
                .totalRuns(this.totalRuns + runs)
                .totalBallsFaced(this.totalBallsFaced + ballsFaced)
                .totalInnings(this.totalInnings + 1)
                .totalNotOuts(this.totalNotOuts + (notOut ? 1 : 0))
                .hundreds(this.hundreds + (runs >= 100 ? 1 : 0))
                .fifties(this.fifties + (runs >= 50 && runs < 100 ? 1 : 0))
                .highestScore(Math.max(this.highestScore, runs))
                .totalWickets(this.totalWickets)
                .totalOversBowled(this.totalOversBowled)
                .totalRunsConceded(this.totalRunsConceded)
                .fiveWicketHauls(this.fiveWicketHauls)
                .tenWicketHauls(this.tenWicketHauls)
                .bestBowlingFigures(this.bestBowlingFigures)
                .totalMatches(this.totalMatches)
                .totalCatches(this.totalCatches)
                .totalRunOuts(this.totalRunOuts)
                .totalStumpings(this.totalStumpings)
                .build();
    }

    /**
     * Add bowling performance to existing stats.
     */
    public PlayerStats addBowlingInnings(int wickets, int runsConceded, int oversBowled) {
        return PlayerStats.builder()
                .totalRuns(this.totalRuns)
                .totalBallsFaced(this.totalBallsFaced)
                .totalInnings(this.totalInnings)
                .totalNotOuts(this.totalNotOuts)
                .hundreds(this.hundreds)
                .fifties(this.fifties)
                .highestScore(this.highestScore)
                .totalWickets(this.totalWickets + wickets)
                .totalOversBowled(this.totalOversBowled + oversBowled)
                .totalRunsConceded(this.totalRunsConceded + runsConceded)
                .fiveWicketHauls(this.fiveWicketHauls + (wickets >= 5 ? 1 : 0))
                .tenWicketHauls(this.tenWicketHauls + (wickets >= 10 ? 1 : 0))
                .bestBowlingFigures(Math.max(this.bestBowlingFigures, wickets))
                .totalMatches(this.totalMatches)
                .totalCatches(this.totalCatches)
                .totalRunOuts(this.totalRunOuts)
                .totalStumpings(this.totalStumpings)
                .build();
    }
}