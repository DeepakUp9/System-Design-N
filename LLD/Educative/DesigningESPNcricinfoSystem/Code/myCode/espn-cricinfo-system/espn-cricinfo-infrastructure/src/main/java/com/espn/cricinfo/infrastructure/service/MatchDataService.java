package com.espn.cricinfo.infrastructure.service;

import com.espn.cricinfo.domain.enums.MatchFormat;
import com.espn.cricinfo.infrastructure.entity.*;
import com.espn.cricinfo.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Data access service for match-related operations.
 * Provides high-level operations combining multiple repositories.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MatchDataService {

    private final MatchRepository matchRepository;
    private final InningsRepository inningsRepository;
    private final BallRepository ballRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;
    private final TournamentRepository tournamentRepository;

    /**
     * Create a new match with full setup.
     */
    @Transactional
    public MatchEntity createMatch(String matchName, MatchFormat format, Long team1Id, Long team2Id,
                                 Long venueId, Long tournamentId, LocalDateTime startTime) {

        TeamEntity team1 = teamRepository.findById(team1Id)
                .orElseThrow(() -> new IllegalArgumentException("Team 1 not found: " + team1Id));

        TeamEntity team2 = teamRepository.findById(team2Id)
                .orElseThrow(() -> new IllegalArgumentException("Team 2 not found: " + team2Id));

        VenueEntity venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found: " + venueId));

        TournamentEntity tournament = null;
        if (tournamentId != null) {
            tournament = tournamentRepository.findById(tournamentId)
                    .orElseThrow(() -> new IllegalArgumentException("Tournament not found: " + tournamentId));
        }

        MatchEntity match = MatchEntity.builder()
                .matchName(matchName)
                .format(format)
                .team1(team1)
                .team2(team2)
                .venue(venue)
                .tournament(tournament)
                .startTime(startTime)
                .build();

        MatchEntity savedMatch = matchRepository.save(match);
        log.info("Created new match: {} (ID: {})", matchName, savedMatch.getId());

        return savedMatch;
    }

    /**
     * Start a match and initialize first innings.
     */
    @Transactional
    public InningsEntity startMatch(Long matchId) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        if (match.getStatus() != MatchEntity.MatchStatus.NOT_STARTED) {
            throw new IllegalStateException("Match is not in NOT_STARTED state");
        }

        // Update match status
        match.setStatus(MatchEntity.MatchStatus.IN_PROGRESS);
        match.setStartTime(LocalDateTime.now());
        match.setCurrentInningsNumber(1);
        match.setIsFirstInnings(true);

        // Create first innings
        InningsEntity innings = InningsEntity.builder()
                .inningsNumber(1)
                .match(match)
                .battingTeam(match.getTeam1())
                .bowlingTeam(match.getTeam2())
                .build();

        InningsEntity savedInnings = inningsRepository.save(innings);
        match.getInnings().add(savedInnings);

        matchRepository.save(match);
        log.info("Started match {} with first innings", match.getMatchName());

        return savedInnings;
    }

    /**
     * Record a ball in the current innings.
     */
    @Transactional
    public BallEntity recordBall(Long matchId, Integer overNumber, Integer ballNumber,
                               String bowlerName, String batsmanName, Integer runsScored,
                               boolean isWicket, boolean isBoundary, boolean isSix,
                               LocalDateTime timestamp) {

        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        InningsEntity currentInnings = getCurrentInnings(match);

        BallEntity ball = BallEntity.builder()
                .match(match)
                .innings(currentInnings)
                .overNumber(overNumber)
                .ballNumber(ballNumber)
                .runsScored(runsScored)
                .isWicket(isWicket)
                .isBoundary(isBoundary)
                .isSix(isSix)
                .bowlerName(bowlerName)
                .batsmanName(batsmanName)
                .timestamp(timestamp)
                .build();

        BallEntity savedBall = ballRepository.save(ball);

        // Update innings statistics
        updateInningsStats(currentInnings, ball);

        // Update match statistics
        match.setTotalRuns(match.getTotalRuns() + ball.getTotalRuns());
        match.setTotalBalls(match.getTotalBalls() + 1);

        if (ball.isOverComplete()) {
            match.setTotalOvers(match.getTotalOvers() + 1);
        }

        matchRepository.save(match);
        inningsRepository.save(currentInnings);

        return savedBall;
    }

    /**
     * Complete current innings and prepare for next innings.
     */
    @Transactional
    public InningsEntity startNextInnings(Long matchId) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        InningsEntity currentInnings = getCurrentInnings(match);
        if (!currentInnings.isCompleted()) {
            throw new IllegalStateException("Current innings is not completed");
        }

        int nextInningsNumber = match.getCurrentInningsNumber() + 1;
        TeamEntity battingTeam = (nextInningsNumber % 2 == 1) ? match.getTeam2() : match.getTeam1();
        TeamEntity bowlingTeam = (nextInningsNumber % 2 == 1) ? match.getTeam1() : match.getTeam2();

        InningsEntity nextInnings = InningsEntity.builder()
                .inningsNumber(nextInningsNumber)
                .match(match)
                .battingTeam(battingTeam)
                .bowlingTeam(bowlingTeam)
                .targetRuns(match.getCurrentInnings().getTotalRuns() + 1)
                .targetOvers((double) match.getMaxOversPerInnings())
                .build();

        InningsEntity savedInnings = inningsRepository.save(nextInnings);
        match.getInnings().add(savedInnings);
        match.setCurrentInningsNumber(nextInningsNumber);
        match.setIsSecondInnings(nextInningsNumber == 2);

        matchRepository.save(match);
        log.info("Started innings {} for match {}", nextInningsNumber, match.getMatchName());

        return savedInnings;
    }

    /**
     * End match and determine winner.
     */
    @Transactional
    public MatchEntity endMatch(Long matchId, Long winnerTeamId, String resultDescription) {
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        if (match.getStatus() != MatchEntity.MatchStatus.IN_PROGRESS) {
            throw new IllegalStateException("Match is not in progress");
        }

        TeamEntity winner = null;
        if (winnerTeamId != null) {
            winner = teamRepository.findById(winnerTeamId)
                    .orElseThrow(() -> new IllegalArgumentException("Winner team not found: " + winnerTeamId));
        }

        match.setWinner(winner);
        match.setResultDescription(resultDescription);
        match.setStatus(MatchEntity.MatchStatus.COMPLETED);
        match.setEndTime(LocalDateTime.now());

        // Update team statistics
        if (winner != null) {
            updateTeamStatistics(match, winner);
        }

        MatchEntity savedMatch = matchRepository.save(match);
        log.info("Ended match {} with result: {}", match.getMatchName(), resultDescription);

        return savedMatch;
    }

    /**
     * Get comprehensive match details with all related data.
     */
    public Optional<MatchEntity> getMatchDetails(Long matchId) {
        return matchRepository.findById(matchId);
    }

    /**
     * Get match statistics and analytics.
     */
    public Map<String, Object> getMatchAnalytics(Long matchId) {
        // This would integrate with the analytics repository
        // For now, return basic statistics
        MatchEntity match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        return Map.of(
                "totalRuns", match.getTotalRuns(),
                "totalWickets", match.getInnings().stream()
                        .mapToInt(InningsEntity::getTotalWickets).sum(),
                "totalOvers", match.getTotalOvers(),
                "inningsCount", match.getInnings().size(),
                "status", match.getStatus()
        );
    }

    /**
     * Search matches with filters.
     */
    public Page<MatchEntity> searchMatches(String teamName, MatchFormat format,
                                         LocalDateTime startDate, LocalDateTime endDate,
                                         Pageable pageable) {
        // Complex search logic would go here
        // For now, return basic search by format
        if (format != null) {
            List<MatchEntity> matches = matchRepository.findByFormat(format);
            // Convert to page (simplified)
            return Page.empty(pageable);
        }

        return Page.empty(pageable);
    }

    /**
     * Get live match updates.
     */
    public List<MatchEntity> getLiveMatches() {
        return matchRepository.findActiveMatches();
    }

    private InningsEntity getCurrentInnings(MatchEntity match) {
        return match.getCurrentInningsNumber() > 0 ?
               match.getInnings().stream()
                   .filter(i -> i.getInningsNumber().equals(match.getCurrentInningsNumber()))
                   .findFirst()
                   .orElseThrow(() -> new IllegalStateException("Current innings not found"))
               : null;
    }

    private void updateInningsStats(InningsEntity innings, BallEntity ball) {
        innings.setTotalRuns(innings.getTotalRuns() + ball.getTotalRuns());
        innings.setTotalBalls(innings.getTotalBalls() + 1);

        if (ball.isOverComplete()) {
            innings.setTotalOvers(innings.getTotalOvers() + 1);
        }

        if (ball.isWicket()) {
            innings.setTotalWickets(innings.getTotalWickets() + 1);
        }
    }

    private void updateTeamStatistics(MatchEntity match, TeamEntity winner) {
        TeamEntity loser = winner.equals(match.getTeam1()) ? match.getTeam2() : match.getTeam1();

        winner.updateStatistics(true, false, false); // Won
        loser.updateStatistics(false, true, false);  // Lost

        teamRepository.save(winner);
        teamRepository.save(loser);
    }
}
