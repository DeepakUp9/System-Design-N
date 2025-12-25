package com.espn.cricinfo.core.strategy;

import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.entities.Match;
import com.espn.cricinfo.domain.entities.Team;
import com.espn.cricinfo.domain.enums.BallType;
import com.espn.cricinfo.domain.enums.MatchFormat;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MatchFormatStrategy implementations.
 * Demonstrates the Strategy Pattern in action.
 */
class MatchFormatStrategyTest {

    private MatchFormatContext context;
    private Ball validBall;
    private Ball invalidBall;

    @BeforeEach
    void setUp() {
        context = new MatchFormatContext();

        // Create a valid ball
        validBall = Ball.builder()
                .id(1L)
                .overNumber(5)
                .ballNumber(3)
                .ballType(BallType.LEGAL_DELIVERY)
                .runsScored(4)
                .isBoundary(true)
                .isSix(false)
                .bowlerName("James Anderson")
                .batsmanName("Virat Kohli")
                .timestamp(LocalDateTime.now())
                .build();

        // Create an invalid ball (invalid ball number)
        invalidBall = Ball.builder()
                .id(2L)
                .overNumber(5)
                .ballNumber(7) // Invalid: should be 1-6
                .ballType(BallType.LEGAL_DELIVERY)
                .runsScored(4)
                .bowlerName("James Anderson")
                .batsmanName("Virat Kohli")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    void testT20Strategy() {
        // Arrange
        context.setStrategy(MatchFormat.T20);
        Match t20Match = createMatch(MatchFormat.T20);

        // Act & Assert
        assertEquals("T20", context.getCurrentStrategyName());
        assertTrue(context.processBall(validBall));
        assertFalse(context.processBall(invalidBall));
        assertEquals(20, context.getMaxOversPerInnings());
        assertEquals(2, context.getMaxInnings());
        assertFalse(context.canDeclareInnings());

        ScoringResult result = context.calculateScore(validBall);
        assertEquals(4, result.getRunsScored());
        assertEquals(BallType.LEGAL_DELIVERY, result.getBallType());
        assertTrue(result.isBoundary());
        assertFalse(result.isSix());
    }

    @Test
    void testODIStrategy() {
        // Arrange
        context.setStrategy(MatchFormat.ODI);
        Match odiMatch = createMatch(MatchFormat.ODI);

        // Act & Assert
        assertEquals("ODI", context.getCurrentStrategyName());
        assertTrue(context.processBall(validBall));
        assertFalse(context.processBall(invalidBall));
        assertEquals(50, context.getMaxOversPerInnings());
        assertEquals(2, context.getMaxInnings());
        assertFalse(context.canDeclareInnings());

        ScoringResult result = context.calculateScore(validBall);
        assertEquals(4, result.getRunsScored());
        assertEquals(BallType.LEGAL_DELIVERY, result.getBallType());
        assertTrue(result.isBoundary());
    }

    @Test
    void testTestStrategy() {
        // Arrange
        context.setStrategy(MatchFormat.TEST);
        Match testMatch = createMatch(MatchFormat.TEST);

        // Act & Assert
        assertEquals("Test", context.getCurrentStrategyName());
        assertTrue(context.processBall(validBall));
        assertFalse(context.processBall(invalidBall));
        assertEquals(0, context.getMaxOversPerInnings()); // Unlimited
        assertEquals(4, context.getMaxInnings());
        assertTrue(context.canDeclareInnings());

        ScoringResult result = context.calculateScore(validBall);
        assertEquals(4, result.getRunsScored());
        assertEquals(BallType.LEGAL_DELIVERY, result.getBallType());
    }

    @Test
    void testStrategySwitching() {
        // Start with T20
        context.setStrategy(MatchFormat.T20);
        assertEquals("T20", context.getCurrentStrategyName());
        assertEquals(20, context.getMaxOversPerInnings());

        // Switch to ODI
        context.setStrategy(MatchFormat.ODI);
        assertEquals("ODI", context.getCurrentStrategyName());
        assertEquals(50, context.getMaxOversPerInnings());

        // Switch to Test
        context.setStrategy(MatchFormat.TEST);
        assertEquals("Test", context.getCurrentStrategyName());
        assertEquals(0, context.getMaxOversPerInnings());
    }

    @Test
    void testNoBallScoring() {
        // Arrange
        context.setStrategy(MatchFormat.T20);
        Ball noBall = Ball.builder()
                .id(3L)
                .overNumber(5)
                .ballNumber(3)
                .ballType(BallType.NO_BALL)
                .runsScored(2)
                .bowlerName("James Anderson")
                .batsmanName("Virat Kohli")
                .timestamp(LocalDateTime.now())
                .build();

        // Act
        ScoringResult result = context.calculateScore(noBall);

        // Assert
        assertEquals(2, result.getRunsScored());
        assertEquals(1, result.getExtraRuns());
        assertEquals(3, result.getTotalRuns());
        assertEquals(BallType.NO_BALL, result.getBallType());
        assertTrue(result.getDescription().contains("No ball"));
    }

    @Test
    void testWideScoring() {
        // Arrange
        context.setStrategy(MatchFormat.T20);
        Ball wide = Ball.builder()
                .id(4L)
                .overNumber(5)
                .ballNumber(3)
                .ballType(BallType.WIDE)
                .runsScored(1)
                .bowlerName("James Anderson")
                .batsmanName("Virat Kohli")
                .timestamp(LocalDateTime.now())
                .build();

        // Act
        ScoringResult result = context.calculateScore(wide);

        // Assert
        assertEquals(1, result.getRunsScored());
        assertEquals(1, result.getExtraRuns());
        assertEquals(2, result.getTotalRuns());
        assertEquals(BallType.WIDE, result.getBallType());
        assertTrue(result.getDescription().contains("Wide"));
    }

    @Test
    void testIllegalStateExceptionWhenNoStrategy() {
        // Arrange
        MatchFormatContext emptyContext = new MatchFormatContext();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> emptyContext.processBall(validBall));
        assertThrows(IllegalStateException.class, () -> emptyContext.calculateScore(validBall));
        assertThrows(IllegalStateException.class, () -> emptyContext.getMaxOversPerInnings());
    }

    private Match createMatch(MatchFormat format) {
        Team team1 = Team.builder()
                .id(1L)
                .name("India")
                .shortName("IND")
                .build();

        Team team2 = Team.builder()
                .id(2L)
                .name("England")
                .shortName("ENG")
                .build();

        return Match.builder()
                .id(1L)
                .matchName("India vs England")
                .format(format)
                .team1(team1)
                .team2(team2)
                .status(Match.MatchStatus.IN_PROGRESS)
                .startTime(LocalDateTime.now())
                .build();
    }
}
