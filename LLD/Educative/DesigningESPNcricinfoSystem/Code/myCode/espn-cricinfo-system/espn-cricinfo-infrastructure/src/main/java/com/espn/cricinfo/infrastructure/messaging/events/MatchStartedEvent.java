package com.espn.cricinfo.infrastructure.messaging.events;

/**
 * Event fired when a match starts.
 */
public class MatchStartedEvent extends BaseEvent {

    private final Long inningsId;

    public MatchStartedEvent(String matchId, Long inningsId) {
        super("MatchStarted", matchId, "Match", 1);
        this.inningsId = inningsId;
    }

    public Long getInningsId() {
        return inningsId;
    }
}
