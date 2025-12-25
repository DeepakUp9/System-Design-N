package com.espn.cricinfo.core.strategy;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Match;
import com.espn.cricinfo.domain.enums.BallType;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Strategy implementation for T20 cricket format.
 * T20 rules: 20 overs per innings, maximum 2 innings, fast-paced scoring.
 */
@Slf4j
public class T20MatchStrategy implements MatchFormatStrategy {

    private static final int MAX_OVERS_PER_INNINGS = 20;
    private static final int MAX_INNINGS = 2;
    private static final int MAX_RUNS_PER_BALL = 7; // Including extras

    @Override
    public boolean isMatchComplete(Match match) {
        if (match == null) {
            return false;
        }

        // Match is complete if both teams have batted or innings limit reached
        boolean bothTeamsBatted = match.getCurrentInningsNumber() >= MAX_INNINGS;
        boolean allOversCompleted = match.getCurrentInnings() != null &&
                                   match.getCurrentInnings().getTotalOvers() >= MAX_OVERS_PER_INNINGS;

        // Or if a team wins by reaching target early
        boolean targetReached = isTargetReached(match);

        return bothTeamsBatted || allOversCompleted || targetReached;
    }

    @Override
    public int getMaxOversPerInnings() {
        return MAX_OVERS_PER_INNINGS;
    }

    @Override
    public int getMaxInnings() {
        return MAX_INNINGS;
    }

    @Override
    public boolean canDeclareInnings() {
        return false; // T20 doesn't allow declaration
    }

    @Override
    public boolean validateBall(Ball ball) {
        if (ball == null) {
            log.warn("Ball validation failed: ball is null");
            return false;
        }

        // Validate runs scored (T20 allows maximum 6 + extras)
        if (ball.getRunsScored() > 6) {
            log.warn("Ball validation failed: invalid runs scored {}", ball.getRunsScored());
            return false;
        }

        // Validate ball number (should be 1-6)
        if (ball.getBallNumber() < 1 || ball.getBallNumber() > 6) {
            log.warn("Ball validation failed: invalid ball number {}", ball.getBallNumber());
            return false;
        }

        // Validate over number
        if (ball.getOverNumber() < 0 || ball.getOverNumber() >= MAX_OVERS_PER_INNINGS) {
            log.warn("Ball validation failed: invalid over number {}", ball.getOverNumber());
            return false;
        }

        return true;
    }

    @Override
    public ScoringResult calculateScore(Ball ball) {
        if (!validateBall(ball)) {
            log.error("Invalid ball for T20 format: {}", ball);
            throw new IllegalArgumentException("Invalid ball for T20 format");
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
        return "T20";
    }

    @Override
    public boolean validateMatchState(Match match) {
        if (match == null) {
            return false;
        }

        // Validate innings count
        if (match.getCurrentInningsNumber() > MAX_INNINGS) {
            log.warn("Invalid innings count for T20: {}", match.getCurrentInningsNumber());
            return false;
        }

        // Validate overs in current innings
        if (match.getCurrentInnings() != null &&
            match.getCurrentInnings().getTotalOvers() > MAX_OVERS_PER_INNINGS) {
            log.warn("Invalid overs count for T20 innings: {}", match.getCurrentInnings().getTotalOvers());
            return false;
        }

        return true;
    }

    private boolean isTargetReached(Match match) {
        // In T20, if chasing team reaches target before 20 overs, match ends
        if (match.getCurrentInningsNumber() == 2 && match.getInnings() != null && match.getInnings().size() >= 2) {
            int firstInningsScore = match.getInnings().get(0).getTotalRuns();
            int secondInningsScore = match.getInnings().get(1).getTotalRuns();
            return secondInningsScore > firstInningsScore;
        }
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
