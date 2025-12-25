package com.espn.cricinfo.domain.valueobjects;

import com.espn.cricinfo.domain.enums.BallType;
import lombok.Builder;
import lombok.Value;

/**
 * Immutable value object representing the result of scoring a ball.
 * Contains all scoring information and provides factory methods for common scenarios.
 */
@Value
@Builder
public class ScoringResult {
    int runsScored;
    BallType ballType;
    boolean isWicket;
    boolean isBoundary;
    boolean isSix;
    int extraRuns;
    String description;

    /**
     * Get total runs including extras.
     */
    public int getTotalRuns() {
        return runsScored + extraRuns;
    }

    /**
     * Check if the scoring result is valid.
     */
    public boolean isValid() {
        return runsScored >= 0 && extraRuns >= 0 && ballType != null;
    }

    /**
     * Check if this ball contributes to the over.
     */
    public boolean countsAsBall() {
        return ballType.countsAsLegalBall();
    }

    /**
     * Create a scoring result for a legal delivery.
     */
    public static ScoringResult legalDelivery(int runs) {
        boolean isBoundary = runs == 4;
        boolean isSix = runs == 6;
        return ScoringResult.builder()
                .runsScored(runs)
                .ballType(BallType.LEGAL_DELIVERY)
                .isBoundary(isBoundary)
                .isSix(isSix)
                .extraRuns(0)
                .description(createLegalDeliveryDescription(runs, isBoundary, isSix))
                .build();
    }

    /**
     * Create a scoring result for a no-ball.
     */
    public static ScoringResult noBall(int runs) {
        return ScoringResult.builder()
                .runsScored(runs)
                .ballType(BallType.NO_BALL)
                .extraRuns(1)
                .description(String.format("No ball: %d runs + 1 extra", runs))
                .build();
    }

    /**
     * Create a scoring result for a wide.
     */
    public static ScoringResult wide(int runs) {
        return ScoringResult.builder()
                .runsScored(runs)
                .ballType(BallType.WIDE)
                .extraRuns(1)
                .description(String.format("Wide: %d runs + 1 extra", runs))
                .build();
    }

    /**
     * Create a scoring result for a bye.
     */
    public static ScoringResult bye(int runs) {
        return ScoringResult.builder()
                .runsScored(runs)
                .ballType(BallType.BYE)
                .extraRuns(0)
                .description(String.format("Bye: %d runs", runs))
                .build();
    }

    /**
     * Create a scoring result for a leg bye.
     */
    public static ScoringResult legBye(int runs) {
        return ScoringResult.builder()
                .runsScored(runs)
                .ballType(BallType.LEG_BYE)
                .extraRuns(0)
                .description(String.format("Leg bye: %d runs", runs))
                .build();
    }

    /**
     * Create a scoring result for an overthrow.
     */
    public static ScoringResult overthrow(int runs) {
        return ScoringResult.builder()
                .runsScored(runs)
                .ballType(BallType.OVERTHROW)
                .extraRuns(0)
                .description(String.format("Overthrow: %d runs", runs))
                .build();
    }

    /**
     * Create a scoring result for a wicket.
     */
    public static ScoringResult wicket(BallType ballType, String wicketType) {
        return ScoringResult.builder()
                .runsScored(0)
                .ballType(ballType)
                .isWicket(true)
                .extraRuns(0)
                .description(String.format("Wicket: %s (%s)", wicketType, ballType.name().toLowerCase()))
                .build();
    }

    private static String createLegalDeliveryDescription(int runs, boolean isBoundary, boolean isSix) {
        if (isSix) {
            return "SIX";
        } else if (isBoundary) {
            return "FOUR";
        } else if (runs == 0) {
            return "Dot ball";
        } else if (runs == 1) {
            return "Single";
        } else if (runs == 2) {
            return "Two runs";
        } else if (runs == 3) {
            return "Three runs";
        } else {
            return runs + " runs";
        }
    }
}