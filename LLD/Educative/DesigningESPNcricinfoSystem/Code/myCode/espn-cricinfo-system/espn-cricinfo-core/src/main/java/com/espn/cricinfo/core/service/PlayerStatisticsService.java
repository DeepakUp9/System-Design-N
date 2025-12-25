package com.espn.cricinfo.core.service;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Player;
import com.espn.cricinfo.domain.enums.MatchFormat;
import com.espn.cricinfo.domain.valueobjects.PlayerStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for calculating and managing player statistics.
 */
@Service
@Slf4j
public class PlayerStatisticsService {

    /**
     * Update player statistics after a ball.
     */
    public void updatePlayerStatistics(Player player, Ball ball, MatchFormat format) {
        log.debug("Updating statistics for player {} after ball: {}",
                 player.getName(), ball.getBallDescription());

        PlayerStats currentStats = player.getCareerStats();
        if (currentStats == null) {
            currentStats = PlayerStats.empty();
        }

        PlayerStats updatedStats = updateStatsForBall(currentStats, ball);

        // Update format-specific stats
        Map<MatchFormat, PlayerStats> formatStats = player.getStatsByFormat();
        if (formatStats == null) {
            formatStats = new HashMap<>();
        }

        PlayerStats formatSpecificStats = formatStats.getOrDefault(format, PlayerStats.empty());
        PlayerStats updatedFormatStats = updateStatsForBall(formatSpecificStats, ball);

        formatStats.put(format, updatedFormatStats);
        player.setStatsByFormat(formatStats);

        log.debug("Updated career stats for {}: {} runs, {} wickets",
                 player.getName(),
                 updatedStats.getTotalRuns(),
                 updatedStats.getTotalWickets());
    }

    /**
     * Calculate batting statistics for a player.
     */
    public BattingStatistics calculateBattingStatistics(Player player, MatchFormat format) {
        PlayerStats stats = format != null ?
                player.getStatsForFormat(format) : player.getCareerStats();

        if (stats == null) {
            stats = PlayerStats.empty();
        }

        return BattingStatistics.builder()
                .totalRuns(stats.getTotalRuns())
                .totalBallsFaced(stats.getTotalBallsFaced())
                .totalInnings(stats.getTotalInnings())
                .totalNotOuts(stats.getTotalNotOuts())
                .battingAverage(stats.getBattingAverage())
                .battingStrikeRate(stats.getBattingStrikeRate())
                .hundreds(stats.getHundreds())
                .fifties(stats.getFifties())
                .highestScore(stats.getHighestScore())
                .build();
    }

    /**
     * Calculate bowling statistics for a player.
     */
    public BowlingStatistics calculateBowlingStatistics(Player player, MatchFormat format) {
        PlayerStats stats = format != null ?
                player.getStatsForFormat(format) : player.getCareerStats();

        if (stats == null) {
            stats = PlayerStats.empty();
        }

        return BowlingStatistics.builder()
                .totalWickets(stats.getTotalWickets())
                .totalOversBowled(stats.getTotalOversBowled())
                .totalRunsConceded(stats.getTotalRunsConceded())
                .bowlingAverage(stats.getBowlingAverage())
                .bowlingEconomy(stats.getBowlingEconomy())
                .bowlingStrikeRate(stats.getBowlingStrikeRate())
                .fiveWicketHauls(stats.getFiveWicketHauls())
                .tenWicketHauls(stats.getTenWicketHauls())
                .bestBowlingFigures(stats.getBestBowlingFigures())
                .build();
    }

    /**
     * Get player ranking based on performance.
     */
    public PlayerRanking getPlayerRanking(Player player, String category) {
        PlayerStats careerStats = player.getCareerStats();
        if (careerStats == null) {
            return new PlayerRanking(player.getName(), category, 0.0, 0);
        }

        double rankingScore = switch (category.toLowerCase()) {
            case "batting" -> calculateBattingRanking(careerStats);
            case "bowling" -> calculateBowlingRanking(careerStats);
            case "allrounder" -> calculateAllRounderRanking(careerStats);
            default -> 0.0;
        };

        return new PlayerRanking(player.getName(), category, rankingScore,
                               careerStats.getTotalMatches());
    }

    /**
     * Check for batting milestones.
     */
    public Milestones checkBattingMilestones(Player player) {
        PlayerStats stats = player.getCareerStats();
        if (stats == null) return new Milestones();

        return Milestones.builder()
                .century(stats.getHundreds() > 0)
                .doubleCentury(stats.getHighestScore() >= 200)
                .thousandRuns(stats.getTotalRuns() >= 1000)
                .fiveThousandRuns(stats.getTotalRuns() >= 5000)
                .tenThousandRuns(stats.getTotalRuns() >= 10000)
                .build();
    }

    /**
     * Check for bowling milestones.
     */
    public Milestones checkBowlingMilestones(Player player) {
        PlayerStats stats = player.getCareerStats();
        if (stats == null) return new Milestones();

        return Milestones.builder()
                .fiveWicketHaul(stats.getFiveWicketHauls() > 0)
                .tenWicketHaul(stats.getTenWicketHauls() > 0)
                .hundredWickets(stats.getTotalWickets() >= 100)
                .twoHundredWickets(stats.getTotalWickets() >= 200)
                .threeHundredWickets(stats.getTotalWickets() >= 300)
                .fourHundredWickets(stats.getTotalWickets() >= 400)
                .build();
    }

    private PlayerStats updateStatsForBall(PlayerStats currentStats, Ball ball) {
        // This is a simplified implementation
        // In a real system, this would be more sophisticated
        if (ball.getBallType().countsAsBall()) {
            // Update batting stats if player was batting
            if (ball.getBatsmanName() != null) {
                return currentStats.addBattingInnings(
                        ball.getRunsScored(),
                        false, // Not out for this ball
                        1 // One ball faced
                );
            }

            // Update bowling stats if player was bowling
            if (ball.getBowlerName() != null) {
                return currentStats.addBowlingInnings(
                        ball.isWicket() ? 1 : 0, // Wickets taken
                        ball.getTotalRuns(), // Runs conceded
                        ball.isOverComplete() ? 1 : 0 // Overs bowled
                );
            }
        }

        return currentStats;
    }

    private double calculateBattingRanking(PlayerStats stats) {
        // Simplified ranking algorithm
        double average = stats.getBattingAverage();
        double strikeRate = stats.getBattingStrikeRate();
        int hundreds = stats.getHundreds();

        return (average * 0.4) + (strikeRate * 0.003) + (hundreds * 10);
    }

    private double calculateBowlingRanking(PlayerStats stats) {
        // Simplified ranking algorithm
        double average = Math.max(0, 40 - stats.getBowlingAverage()); // Lower average is better
        double economy = Math.max(0, 8 - stats.getBowlingEconomy()); // Lower economy is better
        int wickets = stats.getTotalWickets();

        return (average * 0.3) + (economy * 2) + (wickets * 0.1);
    }

    private double calculateAllRounderRanking(PlayerStats stats) {
        return calculateBattingRanking(stats) * 0.5 + calculateBowlingRanking(stats) * 0.5;
    }

    // DTOs for statistics
    public record BattingStatistics(
            int totalRuns,
            int totalBallsFaced,
            int totalInnings,
            int totalNotOuts,
            double battingAverage,
            double battingStrikeRate,
            int hundreds,
            int fifties,
            int highestScore
    ) {}

    public record BowlingStatistics(
            int totalWickets,
            int totalOversBowled,
            int totalRunsConceded,
            double bowlingAverage,
            double bowlingEconomy,
            double bowlingStrikeRate,
            int fiveWicketHauls,
            int tenWicketHauls,
            int bestBowlingFigures
    ) {}

    public record PlayerRanking(
            String playerName,
            String category,
            double rankingScore,
            int totalMatches
    ) {}

    public record Milestones(
            boolean century,
            boolean doubleCentury,
            boolean thousandRuns,
            boolean fiveThousandRuns,
            boolean tenThousandRuns,
            boolean fiveWicketHaul,
            boolean tenWicketHaul,
            boolean hundredWickets,
            boolean twoHundredWickets,
            boolean threeHundredWickets,
            boolean fourHundredWickets
    ) {
        public Milestones() {
            this(false, false, false, false, false, false, false, false, false, false, false);
        }
    }
}
