package com.espn.cricinfo.domain.enums;

/**
 * Types of balls that can be bowled in cricket.
 * Used for scoring calculations in Strategy Pattern.
 */
public enum BallType {
    LEGAL_DELIVERY(false, false, false),
    NO_BALL(true, true, true),
    WIDE(true, false, true),
    BYE(false, true, false),
    LEG_BYE(false, true, false),
    OVERTHROW(false, false, true);

    private final boolean isExtra;
    private final boolean isPenalty;
    private final boolean canScoreRuns;

    BallType(boolean isExtra, boolean isPenalty, boolean canScoreRuns) {
        this.isExtra = isExtra;
        this.isPenalty = isPenalty;
        this.canScoreRuns = canScoreRuns;
    }

    public boolean isExtra() {
        return isExtra;
    }

    public boolean isPenalty() {
        return isPenalty;
    }

    public boolean canScoreRuns() {
        return canScoreRuns;
    }

    public boolean countsAsLegalBall() {
        return !isExtra || this == NO_BALL;
    }
}