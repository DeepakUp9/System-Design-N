package com.espn.cricinfo.domain.entities;

import com.espn.cricinfo.domain.enums.BallType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entity representing a ball bowled in cricket.
 * Used by scoring strategies to calculate match scores.
 */
@Data
@Builder
public class Ball {
    private Long id;
    private int overNumber;
    private int ballNumber;
    private BallType ballType;
    private int runsScored;
    private boolean isWicket;
    private boolean isBoundary;
    private boolean isSix;
    private String bowlerName;
    private String batsmanName;
    private LocalDateTime timestamp;

    // Additional metadata for advanced analytics
    private Double ballSpeed; // km/h
    private String pitchLocation; // e.g., "off stump", "leg stump"
    private String shotPlayed; // e.g., "cover drive", "pull shot"

    public boolean isLegalDelivery() {
        return ballType == BallType.LEGAL_DELIVERY;
    }

    public boolean isExtra() {
        return ballType.isExtra();
    }

    public int getTotalRuns() {
        int extraRuns = switch (ballType) {
            case NO_BALL, WIDE -> 1;
            default -> 0;
        };
        return runsScored + extraRuns;
    }

    public boolean isOverComplete() {
        return ballNumber == 6 && isLegalDelivery();
    }

    public String getBallDescription() {
        return String.format("Over %d.%d: %s to %s - %d runs",
                overNumber, ballNumber, bowlerName, batsmanName, runsScored);
    }
}