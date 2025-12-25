package com.espn.cricinfo.infrastructure.messaging.events;

import java.time.LocalDateTime;

/**
 * Event fired when a match is completed.
 */
public class MatchCompletedEvent extends BaseEvent {

    private final Long winnerTeamId;
    private final String resultType;
    private final String resultDescription;
    private final LocalDateTime endTime;
    private final Long manOfTheMatchPlayerId;

    public MatchCompletedEvent(String matchId, Long winnerTeamId, String resultType,
                             String resultDescription, LocalDateTime endTime,
                             Long manOfTheMatchPlayerId) {
        super("MatchCompleted", matchId, "Match", 1);
        this.winnerTeamId = winnerTeamId;
        this.resultType = resultType;
        this.resultDescription = resultDescription;
        this.endTime = endTime;
        this.manOfTheMatchPlayerId = manOfTheMatchPlayerId;
    }

    // Getters
    public Long getWinnerTeamId() { return winnerTeamId; }
    public String getResultType() { return resultType; }
    public String getResultDescription() { return resultDescription; }
    public LocalDateTime getEndTime() { return endTime; }
    public Long getManOfTheMatchPlayerId() { return manOfTheMatchPlayerId; }
}
