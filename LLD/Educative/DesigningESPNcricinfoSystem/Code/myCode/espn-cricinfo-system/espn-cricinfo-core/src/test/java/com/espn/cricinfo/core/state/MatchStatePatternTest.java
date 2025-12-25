package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.*;
import com.espn.cricinfo.domain.enums.MatchFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test for the State Pattern implementation.
 * Tests match, innings, and player state transitions.
 */
class MatchStatePatternTest {

    private Match testMatch;
    private Team team1;
    private Team team2;
    private Venue venue;

    @BeforeEach
    void setUp() {
        // Create teams
        team1 = Team.builder()
                .id(1L)
                .name("India")
                .shortName("IND")
                .build();

        team2 = Team.builder()
                .id(2L)
                .name("Australia")
                .shortName("AUS")
                .build();

        // Create venue
        venue = Venue.builder()
                .id(1L)
                .name("Melbourne Cricket Ground")
                .city("Melbourne")
                .country("Australia")
                .build();

        // Create match
        testMatch = Match.builder()
                .id(1L)
                .matchName("India vs Australia")
                .format(MatchFormat.T20)
                .team1(team1)
                .team2(team2)
                .venue(venue)
                .startTime(LocalDateTime.now())
                .build();
    }

    @Test
    void testMatchStateTransitions() {
        // Test match context
        MatchContext matchContext = new MatchContext(testMatch);

        // Initial state should be NOT_STARTED
        assertEquals("NOT_STARTED", matchContext.getCurrentStateName());
        assertFalse(matchContext.isActive());
        assertFalse(matchContext.isTerminalState());

        // Start match
        matchContext.startMatch();
        assertEquals("IN_PROGRESS", matchContext.getCurrentStateName());
        assertTrue(matchContext.isActive());
        assertFalse(matchContext.isTerminalState());

        // Pause match
        matchContext.pauseMatch();
        assertEquals("PAUSED", matchContext.getCurrentStateName());
        assertTrue(matchContext.isActive());
        assertFalse(matchContext.isTerminalState());

        // Resume match
        matchContext.resumeMatch();
        assertEquals("IN_PROGRESS", matchContext.getCurrentStateName());
        assertTrue(matchContext.isActive());

        // End match
        matchContext.endMatch();
        assertEquals("COMPLETED", matchContext.getCurrentStateName());
        assertFalse(matchContext.isActive());
        assertTrue(matchContext.isTerminalState());
    }

    @Test
    void testInvalidMatchStateTransitions() {
        MatchContext matchContext = new MatchContext(testMatch);

        // Cannot pause match before starting
        assertThrows(IllegalStateException.class, matchContext::pauseMatch);

        // Cannot resume match before starting
        assertThrows(IllegalStateException.class, matchContext::resumeMatch);

        // Cannot end match before starting
        assertThrows(IllegalStateException.class, matchContext::endMatch);

        // Start match
        matchContext.startMatch();

        // Cannot start match again
        assertThrows(IllegalStateException.class, matchContext::startMatch);

        // End match
        matchContext.endMatch();

        // Cannot perform any actions on completed match
        assertThrows(IllegalStateException.class, matchContext::startMatch);
        assertThrows(IllegalStateException.class, matchContext::pauseMatch);
        assertThrows(IllegalStateException.class, matchContext::resumeMatch);
        assertThrows(IllegalStateException.class, matchContext::endMatch);
    }

    @Test
    void testInningsStateTransitions() {
        // Create innings
        Innings innings = Innings.builder()
                .id(1L)
                .inningsNumber(1)
                .battingTeam(team1)
                .bowlingTeam(team2)
                .build();

        InningsContext inningsContext = new InningsContext(innings);

        // Initial state should be NOT_STARTED
        assertEquals("NOT_STARTED", inningsContext.getCurrentStateName());
        assertFalse(inningsContext.isActive());
        assertFalse(inningsContext.isTerminalState());

        // Start innings
        inningsContext.startInnings();
        assertEquals("IN_PROGRESS", inningsContext.getCurrentStateName());
        assertTrue(inningsContext.isActive());
        assertFalse(inningsContext.isTerminalState());

        // Complete innings
        inningsContext.completeInnings();
        assertEquals("COMPLETED", inningsContext.getCurrentStateName());
        assertFalse(inningsContext.isActive());
        assertTrue(inningsContext.isTerminalState());
    }

    @Test
    void testPlayerStateTransitions() {
        // Create player
        Player player = Player.builder()
                .id(1L)
                .name("Virat Kohli")
                .role("BATSMAN")
                .build();

        PlayerContext playerContext = new PlayerContext(player);

        // Initial state should be NOT_BATTING
        assertEquals("NOT_BATTING", playerContext.getCurrentStateName());
        assertFalse(playerContext.isBatting());
        assertFalse(playerContext.isBowling());
        assertFalse(playerContext.isOut());

        // Player enters field
        playerContext.enterField();
        assertEquals("NOT_BATTING", playerContext.getCurrentStateName());

        // Start batting
        playerContext.startBatting();
        assertEquals("BATTING", playerContext.getCurrentStateName());
        assertTrue(playerContext.isBatting());
        assertFalse(playerContext.isBowling());
        assertFalse(playerContext.isOut());

        // Get out
        playerContext.getOut("CAUGHT");
        assertEquals("OUT", playerContext.getCurrentStateName());
        assertFalse(playerContext.isBatting());
        assertFalse(playerContext.isBowling());
        assertTrue(playerContext.isOut());

        // Leave field
        playerContext.leaveField();
        assertEquals("NOT_BATTING", playerContext.getCurrentStateName());
        assertFalse(playerContext.isBatting());
        assertFalse(playerContext.isBowling());
        assertFalse(playerContext.isOut());
    }

    @Test
    void testBowlerStateTransitions() {
        // Create bowler
        Player bowler = Player.builder()
                .id(2L)
                .name("Jasprit Bumrah")
                .role("BOWLER")
                .build();

        PlayerContext bowlerContext = new PlayerContext(bowler);

        // Initial state should be NOT_BATTING
        assertEquals("NOT_BATTING", bowlerContext.getCurrentStateName());

        // Enter field
        bowlerContext.enterField();

        // Start bowling
        bowlerContext.startBowling();
        assertEquals("BOWLING", bowlerContext.getCurrentStateName());
        assertFalse(bowlerContext.isBatting());
        assertTrue(bowlerContext.isBowling());
        assertFalse(bowlerContext.isOut());

        // Leave field (finish bowling)
        bowlerContext.leaveField();
        assertEquals("NOT_BATTING", bowlerContext.getCurrentStateName());
        assertFalse(bowlerContext.isBatting());
        assertFalse(bowlerContext.isBowling());
        assertFalse(bowlerContext.isOut());
    }

    @Test
    void testAllRounderStateTransitions() {
        // Create all-rounder
        Player allRounder = Player.builder()
                .id(3L)
                .name("Ben Stokes")
                .role("ALL_ROUNDER")
                .build();

        PlayerContext allRounderContext = new PlayerContext(allRounder);

        // Start as batsman
        allRounderContext.enterField();
        allRounderContext.startBatting();
        assertEquals("BATTING", allRounderContext.getCurrentStateName());
        assertTrue(allRounderContext.canBat());
        assertTrue(allRounderContext.canBowl());

        // Get out
        allRounderContext.getOut("LBW");
        assertEquals("OUT", allRounderContext.getCurrentStateName());

        // Start bowling (can bowl even when out)
        allRounderContext.startBowling();
        assertEquals("BOWLING", allRounderContext.getCurrentStateName());
        assertTrue(allRounderContext.isBowling());
        assertTrue(allRounderContext.isOut()); // Still out from batting
    }

    @Test
    void testInvalidPlayerStateTransitions() {
        Player batsman = Player.builder()
                .id(4L)
                .name("Steve Smith")
                .role("BATSMAN")
                .build();

        PlayerContext batsmanContext = new PlayerContext(batsman);

        // Cannot get out before batting
        assertThrows(IllegalStateException.class, () -> batsmanContext.getOut("CAUGHT"));

        // Start batting
        batsmanContext.enterField();
        batsmanContext.startBatting();

        // Cannot start batting again
        assertThrows(IllegalStateException.class, batsmanContext::startBatting);

        // Cannot start bowling while batting
        assertThrows(IllegalStateException.class, batsmanContext::startBowling);

        // Get out
        batsmanContext.getOut("BOWLED");

        // Cannot get out again
        assertThrows(IllegalStateException.class, () -> batsmanContext.getOut("CAUGHT"));

        // Cannot start batting again (already out)
        assertThrows(IllegalStateException.class, batsmanContext::startBatting);
    }

    @Test
    void testMatchInningsIntegration() {
        // Create match and innings
        MatchContext matchContext = new MatchContext(testMatch);
        Innings innings = Innings.builder()
                .id(1L)
                .inningsNumber(1)
                .battingTeam(team1)
                .bowlingTeam(team2)
                .build();
        InningsContext inningsContext = new InningsContext(innings);

        // Start match
        matchContext.startMatch();
        assertTrue(matchContext.canStartNewInnings());

        // Start innings
        inningsContext.startInnings();

        // Simulate innings completion
        innings.setTotalRuns(150);
        innings.setTotalWickets(5);
        innings.setCompleted(true);

        // Handle innings completion in match
        matchContext.handleInningsCompletion();

        // Match should still be in progress (can start second innings)
        assertEquals("IN_PROGRESS", matchContext.getCurrentStateName());
    }

    @Test
    void testStatePatternIsolation() {
        // Test that state changes are isolated and don't affect other instances
        Match match1 = Match.builder()
                .id(1L)
                .matchName("Match 1")
                .format(MatchFormat.T20)
                .team1(team1)
                .team2(team2)
                .venue(venue)
                .build();

        Match match2 = Match.builder()
                .id(2L)
                .matchName("Match 2")
                .format(MatchFormat.ODI)
                .team1(team1)
                .team2(team2)
                .venue(venue)
                .build();

        MatchContext context1 = new MatchContext(match1);
        MatchContext context2 = new MatchContext(match2);

        // Start first match
        context1.startMatch();
        assertEquals("IN_PROGRESS", context1.getCurrentStateName());
        assertEquals("NOT_STARTED", context2.getCurrentStateName());

        // Start second match
        context2.startMatch();
        assertEquals("IN_PROGRESS", context1.getCurrentStateName());
        assertEquals("IN_PROGRESS", context2.getCurrentStateName());

        // End first match
        context1.endMatch();
        assertEquals("COMPLETED", context1.getCurrentStateName());
        assertEquals("IN_PROGRESS", context2.getCurrentStateName());
    }
}
