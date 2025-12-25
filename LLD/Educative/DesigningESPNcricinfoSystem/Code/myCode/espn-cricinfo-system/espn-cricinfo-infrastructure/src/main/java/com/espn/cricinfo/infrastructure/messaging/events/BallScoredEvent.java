package com.espn.cricinfo.infrastructure.messaging.events;

import com.espn.cricinfo.domain.enums.BallType;

/**
 * Event fired when a ball is scored in a match.
 */
public class BallScoredEvent extends BaseEvent {

    private final Long ballId;
    private final Integer overNumber;
    private final Integer ballNumber;
    private final BallType ballType;
    private final Integer runsScored;
    private final Boolean isWicket;
    private final Boolean isBoundary;
    private final Boolean isSix;
    private final String bowlerName;
    private final String batsmanName;
    private final String commentary;

    public BallScoredEvent(String matchId, Long ballId, Integer overNumber, Integer ballNumber,
                         BallType ballType, Integer runsScored, Boolean isWicket,
                         Boolean isBoundary, Boolean isSix, String bowlerName,
                         String batsmanName, String commentary) {
        super("BallScored", matchId, "Match", 1);
        this.ballId = ballId;
        this.overNumber = overNumber;
        this.ballNumber = ballNumber;
        this.ballType = ballType;
        this.runsScored = runsScored;
        this.isWicket = isWicket;
        this.isBoundary = isBoundary;
        this.isSix = isSix;
        this.bowlerName = bowlerName;
        this.batsmanName = batsmanName;
        this.commentary = commentary;
    }

    // Getters
    public Long getBallId() { return ballId; }
    public Integer getOverNumber() { return overNumber; }
    public Integer getBallNumber() { return ballNumber; }
    public BallType getBallType() { return ballType; }
    public Integer getRunsScored() { return runsScored; }
    public Boolean getIsWicket() { return isWicket; }
    public Boolean getIsBoundary() { return isBoundary; }
    public Boolean getIsSix() { return isSix; }
    public String getBowlerName() { return bowlerName; }
    public String getBatsmanName() { return batsmanName; }
    public String getCommentary() { return commentary; }
}
