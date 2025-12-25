package com.espn.cricinfo.core.strategy;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Match;
import com.espn.cricinfo.domain.enums.BallType;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Strategy implementation for Test cricket format.
 * Test rules: Unlimited overs, maximum 2 innings per team, declaration allowed,
 * follow-on rules, innings can be drawn.
 */
@Slf4j
public class TestMatchStrategy implements MatchFormatStrategy {

    private static final int MAX_INNINGS_PER_TEAM = 2;
    private static final int MAX_TOTAL_INNINGS = 4; // 2 per team
    private static final int FOLLOW_ON_MINIMUM_LEAD = 200; // Minimum lead to enforce follow-on
    private static final int MINIMUM_BALLS_BEFORE_DECLARATION = 60; // 10 overs minimum

    @Override
    public boolean isMatchComplete(Match match) {
        if (match == null) {
            return false;
        }

        // Match is complete if all innings are done or result is clear
        boolean allInningsCompleted = match.getCurrentInningsNumber() >= MAX_TOTAL_INNINGS;
        boolean resultDecided = isResultDecided(match);
        boolean matchDrawn = isMatchDrawn(match);

        return allInningsCompleted || resultDecided || matchDrawn;
    }

    @Override
    public int getMaxOversPerInnings() {
        return 0; // Unlimited overs in Test cricket
    }

    @Override
    public int getMaxInnings() {
        return MAX_TOTAL_INNINGS;
    }

    @Override
    public boolean canDeclareInnings() {
        return true; // Test cricket allows declaration
    }

    @Override
    public boolean validateBall(Ball ball) {
        if (ball == null) {
            log.warn("Ball validation failed: ball is null");
            return false;
        }

        // Validate runs scored (Test allows maximum 6 + extras, same as limited overs)
        if (ball.getRunsScored() > 6) {
            log.warn("Ball validation failed: invalid runs scored {}", ball.getRunsScored());
            return false;
        }

        // Validate ball number (should be 1-6)
        if (ball.getBallNumber() < 1 || ball.getBallNumber() > 6) {
            log.warn("Ball validation failed: invalid ball number {}", ball.getBallNumber());
            return false;
        }

        // Validate over number (no upper limit in Test cricket)
        if (ball.getOverNumber() < 0) {
            log.warn("Ball validation failed: invalid over number {}", ball.getOverNumber());
            return false;
        }

        return true;
    }

    @Override
    public ScoringResult calculateScore(Ball ball) {
        if (!validateBall(ball)) {
            log.error("Invalid ball for Test format: {}", ball);
            throw new IllegalArgumentException("Invalid ball for Test format");
        }

        return switch (ball.getBallType()) {
            case LEGAL_DELIVERY -> {
                boolean isBoundary = ball.getRunsScored() == 4;
                boolean isSix = ball.getRunsScored() == 6;
                yield ScoringResult.builder()
                        .runsScored(ball.getRunsScored())
                        .ballType(BallType.LEGAL_DELIVERY)
                        .isWicket(ball.isWicket())
                        .isBoundary(isBoundary)
                        .isSix(isSix)
                        .extraRuns(0)
                        .description(createBallDescription(ball))
                        .build();
            }
            case NO_BALL -> ScoringResult.noBall(ball.getRunsScored());
            case WIDE -> ScoringResult.wide(ball.getRunsScored());
            case BYE -> ScoringResult.builder()
                    .runsScored(ball.getRunsScored())
                    .ballType(BallType.BYE)
                    .isWicket(ball.isWicket())
                    .extraRuns(0)
                    .description("Bye: " + ball.getRunsScored() + " runs")
                    .build();
            case LEG_BYE -> ScoringResult.builder()
                    .runsScored(ball.getRunsScored())
                    .ballType(BallType.LEG_BYE)
                    .isWicket(ball.isWicket())
                    .extraRuns(0)
                    .description("Leg bye: " + ball.getRunsScored() + " runs")
                    .build();
            case OVERTHROW -> ScoringResult.builder()
                    .runsScored(ball.getRunsScored())
                    .ballType(BallType.OVERTHROW)
                    .extraRuns(0)
                    .description("Overthrow: " + ball.getRunsScored() + " runs")
                    .build();
        };
    }

    @Override
    public String getFormatName() {
        return "Test";
    }

    @Override
    public boolean validateMatchState(Match match) {
        if (match == null) {
            return false;
        }

        // Validate innings count
        if (match.getCurrentInningsNumber() > MAX_TOTAL_INNINGS) {
            log.warn("Invalid innings count for Test match: {}", match.getCurrentInningsNumber());
            return false;
        }

        return true;
    }

    /**
     * Validates if an innings can be declared based on Test cricket rules.
     *
     * @param match the current match
     * @return true if declaration is allowed
     */
    public boolean canDeclareInnings(Match match) {
        if (match == null || match.getCurrentInnings() == null) {
            return false;
        }

        // Must have batted for minimum overs (10 overs = 60 balls)
        if (match.getCurrentInnings().getTotalBalls() < MINIMUM_BALLS_BEFORE_DECLARATION) {
            return false;
        }

        // Cannot declare if team is all out
        if (match.getCurrentInnings().isAllOut()) {
            return false;
        }

        // Declaration is generally allowed in Test cricket after minimum batting
        return true;
    }

    /**
     * Calculates if follow-on should be enforced.
     *
     * @param firstInningsScore score of team that batted first
     * @param secondInningsScore score of team that batted second
     * @return true if follow-on should be enforced
     */
    public boolean shouldEnforceFollowOn(int firstInningsScore, int secondInningsScore) {
        int lead = firstInningsScore - secondInningsScore;
        return lead >= FOLLOW_ON_MINIMUM_LEAD;
    }

    /**
     * Checks if the match result is already decided.
     */
    private boolean isResultDecided(Match match) {
        if (match.getInnings() == null || match.getInnings().size() < 2) {
            return false;
        }

        // If team batting last has more runs than opponent can make
        if (match.getCurrentInningsNumber() >= 3) {
            int lastBattingTeamScore = match.getInnings().get(match.getInnings().size() - 1).getTotalRuns();
            int opponentTotalScore = match.getInnings().stream()
                    .limit(match.getInnings().size() - 1)
                    .mapToInt(innings -> innings.getTotalRuns())
                    .sum();

            return lastBattingTeamScore > opponentTotalScore;
        }

        return false;
    }

    /**
     * Checks if the match is drawn (time limit reached).
     */
    private boolean isMatchDrawn(Match match) {
        // In a real implementation, this would check time limits, weather, etc.
        // For now, return false (matches continue until result)
        return false;
    }

    private String createBallDescription(Ball ball) {
        StringBuilder description = new StringBuilder();
        description.append(String.format("Over %d.%d: %s to %s",
                ball.getOverNumber(), ball.getBallNumber(),
                ball.getBowlerName(), ball.getBatsmanName()));

        if (ball.getRunsScored() > 0) {
            description.append(" - ").append(ball.getRunsScored()).append(" runs");
            if (ball.getRunsScored() == 4) {
                description.append(" (FOUR)");
            } else if (ball.getRunsScored() == 6) {
                description.append(" (SIX)");
            }
        } else if (ball.isWicket()) {
            description.append(" - WICKET");
        } else {
            description.append(" - dot ball");
        }

        return description.toString();
    }
}
