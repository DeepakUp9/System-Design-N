package com.espn.cricinfo.domain.enums;

/**
 * Enumeration of different cricket match formats.
 * Used by Strategy Pattern to determine match rules and scoring.
 */
public enum MatchFormat {
    T20("Twenty20", 20, 2, false),
    ODI("One Day International", 50, 2, false),
    TEST("Test Cricket", 0, 2, true),
    T10("Ten10", 10, 2, false),
    HUNDRED("The Hundred", 100, 2, false);

    private final String displayName;
    private final int oversPerInnings;
    private final int maxInnings;
    private final boolean canDeclareInnings;

    MatchFormat(String displayName, int oversPerInnings, int maxInnings, boolean canDeclareInnings) {
        this.displayName = displayName;
        this.oversPerInnings = oversPerInnings;
        this.maxInnings = maxInnings;
        this.canDeclareInnings = canDeclareInnings;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getOversPerInnings() {
        return oversPerInnings;
    }

    public int getMaxInnings() {
        return maxInnings;
    }

    public boolean canDeclareInnings() {
        return canDeclareInnings;
    }

    public boolean isLimitedOvers() {
        return oversPerInnings > 0;
    }
}