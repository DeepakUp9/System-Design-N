package com.espn.cricinfo.core.service;

import com.espn.cricinfo.domain.entities.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Business service for tournament management and scheduling.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TournamentService {

    /**
     * Create a new tournament with teams and scheduling.
     */
    @Transactional
    public Tournament createTournament(String name, Tournament.TournamentFormat format,
                                     LocalDate startDate, LocalDate endDate,
                                     List<Team> teams, List<Venue> venues) {
        log.info("Creating tournament: {} with {} teams", name, teams.size());

        Tournament tournament = Tournament.builder()
                .name(name)
                .format(format)
                .startDate(startDate)
                .endDate(endDate)
                .status(Tournament.TournamentStatus.UPCOMING)
                .totalTeams(teams.size())
                .build();

        // Add teams and venues
        teams.forEach(tournament::addTeam);
        venues.forEach(tournament::addVenue);

        // Generate initial schedule
        List<Match> matches = generateTournamentSchedule(tournament, teams, venues);
        matches.forEach(tournament::addMatch);

        tournament.setTotalMatches(matches.size());

        log.info("Tournament created with {} matches", matches.size());
        return tournament;
    }

    /**
     * Start a tournament.
     */
    @Transactional
    public void startTournament(Tournament tournament) {
        if (tournament.getStatus() != Tournament.TournamentStatus.UPCOMING) {
            throw new IllegalStateException("Tournament is not in UPCOMING status");
        }

        tournament.setStatus(Tournament.TournamentStatus.IN_PROGRESS);
        log.info("Tournament {} started", tournament.getName());
    }

    /**
     * Complete a tournament and determine winner.
     */
    @Transactional
    public void completeTournament(Tournament tournament, Team winner, Team runnerUp) {
        tournament.setWinner(winner);
        tournament.setRunnerUp(runnerUp);
        tournament.setStatus(Tournament.TournamentStatus.COMPLETED);

        log.info("Tournament {} completed. Winner: {}, Runner-up: {}",
                tournament.getName(),
                winner != null ? winner.getName() : "N/A",
                runnerUp != null ? runnerUp.getName() : "N/A");
    }

    /**
     * Get tournament standings.
     */
    public List<TeamStanding> getTournamentStandings(Tournament tournament) {
        return tournament.getTeams().stream()
                .map(team -> calculateTeamStanding(tournament, team))
                .sorted((a, b) -> {
                    // Sort by points, then run rate
                    if (a.points() != b.points()) {
                        return Integer.compare(b.points(), a.points());
                    }
                    return Double.compare(b.netRunRate(), a.netRunRate());
                })
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming matches in tournament.
     */
    public List<Match> getUpcomingMatches(Tournament tournament) {
        return tournament.getMatches().stream()
                .filter(match -> match.getStatus() == Match.MatchStatus.NOT_STARTED)
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                .collect(Collectors.toList());
    }

    /**
     * Get completed matches in tournament.
     */
    public List<Match> getCompletedMatches(Tournament tournament) {
        return tournament.getMatches().stream()
                .filter(match -> match.getStatus() == Match.MatchStatus.COMPLETED)
                .sorted((a, b) -> b.getEndTime().compareTo(a.getEndTime()))
                .collect(Collectors.toList());
    }

    /**
     * Update match result in tournament.
     */
    @Transactional
    public void updateMatchResult(Tournament tournament, Match match) {
        // Update tournament statistics
        tournament.setMatchesCompleted(tournament.getMatchesCompleted() + 1);

        // Check if tournament should end
        if (tournament.getMatchesCompleted() >= tournament.getTotalMatches()) {
            determineTournamentWinner(tournament);
        }

        log.info("Match result updated in tournament {}. Completed: {}/{}",
                tournament.getName(), tournament.getMatchesCompleted(), tournament.getTotalMatches());
    }

    /**
     * Generate tournament schedule based on format.
     */
    private List<Match> generateTournamentSchedule(Tournament tournament,
                                                 List<Team> teams,
                                                 List<Venue> venues) {
        List<Match> matches = new ArrayList<>();

        switch (tournament.getFormat()) {
            case WORLD_CUP:
            case LEAGUE:
                matches.addAll(generateLeagueSchedule(teams, venues));
                break;
            case CHAMPIONS_TROPHY:
                matches.addAll(generateRoundRobinSchedule(teams, venues));
                break;
            case TEST_SERIES:
            case ODI_SERIES:
            case T20_SERIES:
                matches.addAll(generateSeriesSchedule(teams, venues, tournament.getFormat()));
                break;
            case KNOCKOUT:
                matches.addAll(generateKnockoutSchedule(teams, venues));
                break;
            default:
                // Default to round-robin
                matches.addAll(generateRoundRobinSchedule(teams, venues));
        }

        return matches;
    }

    private List<Match> generateLeagueSchedule(List<Team> teams, List<Venue> venues) {
        List<Match> matches = new ArrayList<>();
        LocalDate startDate = LocalDate.now().plusDays(1);

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Venue venue = venues.get(j % venues.size());
                Match match = Match.builder()
                        .matchName(teams.get(i).getName() + " vs " + teams.get(j).getName())
                        .format(Match.MatchFormat.T20) // Default to T20 for league
                        .team1(teams.get(i))
                        .team2(teams.get(j))
                        .venue(venue)
                        .startTime(startDate.atTime(14, 0))
                        .build();

                matches.add(match);
                startDate = startDate.plusDays(1);
            }
        }

        return matches;
    }

    private List<Match> generateRoundRobinSchedule(List<Team> teams, List<Venue> venues) {
        // Simplified round-robin implementation
        return generateLeagueSchedule(teams, venues);
    }

    private List<Match> generateSeriesSchedule(List<Team> teams, List<Venue> venues,
                                             Tournament.TournamentFormat format) {
        List<Match> matches = new ArrayList<>();
        LocalDate startDate = LocalDate.now().plusDays(1);

        if (teams.size() >= 2) {
            Team team1 = teams.get(0);
            Team team2 = teams.get(1);

            int matchCount = switch (format) {
                case TEST_SERIES -> 5; // 5 Test matches
                case ODI_SERIES -> 5; // 5 ODI matches
                case T20_SERIES -> 3; // 3 T20 matches
                default -> 3;
            };

            Match.MatchFormat matchFormat = switch (format) {
                case TEST_SERIES -> Match.MatchFormat.TEST;
                case ODI_SERIES -> Match.MatchFormat.ODI;
                case T20_SERIES -> Match.MatchFormat.T20;
                default -> Match.MatchFormat.T20;
            };

            for (int i = 1; i <= matchCount; i++) {
                Venue venue = venues.get((i - 1) % venues.size());
                Match match = Match.builder()
                        .matchName(team1.getName() + " vs " + team2.getName() + " - Match " + i)
                        .format(matchFormat)
                        .team1(team1)
                        .team2(team2)
                        .venue(venue)
                        .startTime(startDate.atTime(10, 0))
                        .build();

                matches.add(match);
                startDate = startDate.plusDays(2); // Gap between series matches
            }
        }

        return matches;
    }

    private List<Match> generateKnockoutSchedule(List<Team> teams, List<Venue> venues) {
        // Simplified knockout implementation
        List<Match> matches = new ArrayList<>();
        LocalDate startDate = LocalDate.now().plusDays(1);

        // Assume 8 teams for quarter-finals, semi-finals, final
        for (int round = 1; round <= 3; round++) {
            int matchesInRound = switch (round) {
                case 1 -> 4; // Quarter-finals
                case 2 -> 2; // Semi-finals
                case 3 -> 1; // Final
                default -> 0;
            };

            for (int i = 0; i < matchesInRound; i++) {
                String roundName = switch (round) {
                    case 1 -> "Quarter-Final";
                    case 2 -> "Semi-Final";
                    case 3 -> "Final";
                    default -> "Match";
                };

                Venue venue = venues.get(i % venues.size());
                Match match = Match.builder()
                        .matchName(roundName + " " + (i + 1))
                        .format(Match.MatchFormat.T20)
                        .venue(venue)
                        .startTime(startDate.atTime(14, 0))
                        .build();

                matches.add(match);
            }
            startDate = startDate.plusDays(3);
        }

        return matches;
    }

    private TeamStanding calculateTeamStanding(Tournament tournament, Team team) {
        // Simplified standings calculation
        // In a real implementation, this would analyze all matches
        return new TeamStanding(team.getName(), 0, 0, 0, 0.0, 0.0);
    }

    private void determineTournamentWinner(Tournament tournament) {
        // Simplified winner determination
        // In a real implementation, this would be based on standings
        if (!tournament.getTeams().isEmpty()) {
            Team winner = tournament.getTeams().get(0);
            tournament.setWinner(winner);
            tournament.setStatus(Tournament.TournamentStatus.COMPLETED);
        }
    }

    // Records for tournament data
    public record TeamStanding(
            String teamName,
            int played,
            int won,
            int lost,
            double netRunRate,
            int points
    ) {}
}
