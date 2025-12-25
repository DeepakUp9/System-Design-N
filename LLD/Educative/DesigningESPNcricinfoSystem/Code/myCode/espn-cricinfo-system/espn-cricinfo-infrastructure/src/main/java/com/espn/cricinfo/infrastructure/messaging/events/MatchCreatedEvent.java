package com.espn.cricinfo.infrastructure.messaging.events;

import com.espn.cricinfo.domain.enums.MatchFormat;

import java.time.LocalDateTime;

/**
 * Event fired when a new match is created.
 */
public class MatchCreatedEvent extends BaseEvent {

    private final String matchName;
    private final MatchFormat format;
    private final Long team1Id;
    private final Long team2Id;
    private final Long venueId;
    private final Long tournamentId;
    private final LocalDateTime startTime;

    public MatchCreatedEvent(String matchId, String matchName, MatchFormat format,
                           Long team1Id, Long team2Id, Long venueId, Long tournamentId,
                           LocalDateTime startTime) {
        super("MatchCreated", matchId, "Match", 1);
        this.matchName = matchName;
        this.format = format;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.venueId = venueId;
        this.tournamentId = tournamentId;
        this.startTime = startTime;
    }

    // Getters
    public String getMatchName() { return matchName; }
    public MatchFormat getFormat() { return format; }
    public Long getTeam1Id() { return team1Id; }
    public Long getTeam2Id() { return team2Id; }
    public Long getVenueId() { return venueId; }
    public Long getTournamentId() { return tournamentId; }
    public LocalDateTime getStartTime() { return startTime; }
}
