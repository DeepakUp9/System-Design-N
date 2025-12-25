package com.espn.cricinfo.core.strategy;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.enums.BallType;
import com.espn.cricinfo.domain.valueobjects.PlayerStats;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Standard cricket scoring strategy implementation.
 * Implements traditional cricket scoring rules used in most formats.
 */
@Slf4j
public class StandardScoringStrategy implements ScoringStrategy {

    private static final int MAX_RUNS_PER_BALL = 6;
    private static final int BONUS_POINTS_PER_CENTURY = 10;
    private static final int BONUS_POINTS_PER_HALF_CENTURY = 5;
    private static final int BONUS_POINTS_PER_FIFTY_WICKETS = 15;

    @Override
    public ScoringResult calculateBallScore(Ball ball) {
        if (ball == null) {
            throw new IllegalArgumentException("Ball cannot be null");
        }

        return switch (ball.getBallType()) {
            case LEGAL_DELIVERY -> calculateLegalDeliveryScore(ball);
            case NO_BALL -> calculateNoBallScore(ball);
            case WIDE -> calculateWideScore(ball);
            case BYE -> calculateByeScore(ball);
            case LEG_BYE -> calculateLegByeScore(ball);
            case OVERTHROW -> calculateOverthrowScore(ball);
        };
    }

    @Override
    public boolean isValidScore(int runs, boolean isExtra) {
        if (runs < 0) {
            return false;
        }

        if (isExtra) {
            // Extras can be any non-negative number
            return true;
        } else {
            // Regular runs: 0-6 (including boundaries)
            return runs >= 0 && runs <= MAX_RUNS_PER_BALL;
        }
    }

    @Override
    public int getMaxRunsPerBall() {
        return MAX_RUNS_PER_BALL;
    }

    @Override
    public int calculateBonusPoints(PlayerStats stats) {
        if (stats == null) {
            return 0;
        }

        int bonusPoints = 0;

        // Batting bonus points
        bonusPoints += stats.getHundreds() * BONUS_POINTS_PER_CENTURY;
        bonusPoints += stats.getFifties() * BONUS_POINTS_PER_HALF_CENTURY;

        // Bowling bonus points
        bonusPoints += stats.getFiveWicketHauls() * BONUS_POINTS_PER_FIFTY_WICKETS;

        return bonusPoints;
    }

    @Override
    public PlayerStats updatePlayerStats(PlayerStats currentStats, Ball ball) {
        if (currentStats == null || ball == null) {
            throw new IllegalArgumentException("Stats and ball cannot be null");
        }

        // This is a simplified implementation
        // In a real system, this would be more complex with proper stat calculations
        PlayerStats.PlayerStatsBuilder builder = currentStats.toBuilder();

        // Update batting stats if the ball was faced by batsman
        if (ball.getBallType() == BallType.LEGAL_DELIVERY && !ball.isWicket()) {
            builder.totalRuns(currentStats.getTotalRuns() + ball.getRunsScored());
            builder.totalBallsFaced(currentStats.getTotalBallsFaced() + 1);

            // Update centuries and fifties
            int newTotalRuns = currentStats.getTotalRuns() + ball.getRunsScored();
            if (newTotalRuns >= 100 && currentStats.getTotalRuns() < 100) {
                builder.hundreds(currentStats.getHundreds() + 1);
            } else if (newTotalRuns >= 50 && currentStats.getTotalRuns() < 50) {
                builder.fifties(currentStats.getFifties() + 1);
            }
        }

        return builder.build();
    }

    @Override
    public String getStrategyName() {
        return "Standard Cricket Scoring";
    }

    private ScoringResult calculateLegalDeliveryScore(Ball ball) {
        boolean isBoundary = ball.getRunsScored() == 4;
        boolean isSix = ball.getRunsScored() == 6;

        return ScoringResult.builder()
                .runsScored(ball.getRunsScored())
                .ballType(BallType.LEGAL_DELIVERY)
                .isWicket(ball.isWicket())
                .isBoundary(isBoundary)
                .isSix(isSix)
                .extraRuns(0)
                .description(createLegalDeliveryDescription(ball))
                .build();
    }

    private ScoringResult calculateNoBallScore(Ball ball) {
        return ScoringResult.builder()
                .runsScored(ball.getRunsScored())
                .ballType(BallType.NO_BALL)
                .isWicket(ball.isWicket())
                .extraRuns(1) // No ball itself counts as 1 extra
                .description("No ball: " + ball.getRunsScored() + " runs + 1 extra")
                .build();
    }

    private ScoringResult calculateWideScore(Ball ball) {
        return ScoringResult.builder()
                .runsScored(ball.getRunsScored())
                .ballType(BallType.WIDE)
                .extraRuns(1) // Wide itself counts as 1 extra
                .description("Wide: " + ball.getRunsScored() + " runs + 1 extra")
                .build();
    }

    private ScoringResult calculateByeScore(Ball ball) {
        return ScoringResult.builder()
                .runsScored(ball.getRunsScored())
                .ballType(BallType.BYE)
                .isWicket(ball.isWicket())
                .extraRuns(0)
                .description("Bye: " + ball.getRunsScored() + " runs")
                .build();
    }

    private ScoringResult calculateLegByeScore(Ball ball) {
        return ScoringResult.builder()
                .runsScored(ball.getRunsScored())
                .ballType(BallType.LEG_BYE)
                .isWicket(ball.isWicket())
                .extraRuns(0)
                .description("Leg bye: " + ball.getRunsScored() + " runs")
                .build();
    }

    private ScoringResult calculateOverthrowScore(Ball ball) {
        return ScoringResult.builder()
                .runsScored(ball.getRunsScored())
                .ballType(BallType.OVERTHROW)
                .extraRuns(0)
                .description("Overthrow: " + ball.getRunsScored() + " runs")
                .build();
    }

    private String createLegalDeliveryDescription(Ball ball) {
        StringBuilder description = new StringBuilder();

        if (ball.getRunsScored() == 0 && !ball.isWicket()) {
            description.append("Dot ball");
        } else if (ball.getRunsScored() == 1) {
            description.append("Single");
        } else if (ball.getRunsScored() == 2) {
            description.append("Two runs");
        } else if (ball.getRunsScored() == 3) {
            description.append("Three runs");
        } else if (ball.getRunsScored() == 4) {
            description.append("FOUR");
        } else if (ball.getRunsScored() == 6) {
            description.append("SIX");
        } else {
            description.append(ball.getRunsScored()).append(" runs");
        }

        if (ball.isWicket()) {
            if (description.length() > 0) {
                description.append(" and ");
            }
            description.append("WICKET");
        }

        return description.toString();
    }
}
