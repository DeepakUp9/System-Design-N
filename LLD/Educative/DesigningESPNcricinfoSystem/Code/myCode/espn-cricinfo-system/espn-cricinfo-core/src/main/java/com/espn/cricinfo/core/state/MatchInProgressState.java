package com.espn.cricinfo.core.state;

import com.espn.cricinfo.domain.entities.Innings;
import com.espn.cricinfo.domain.entities.Match;
import com.espn.cricinfo.domain.enums.MatchFormat;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * State representing a match that is currently in progress.
 * Handles transitions to paused, completed, or abandoned states.
 */
@Slf4j
public class MatchInProgressState implements MatchState {

    @Override
    public String getStateName() {
        return "IN_PROGRESS";
    }

    @Override
    public boolean canStartMatch(Match match) {
        return false; // Match is already started
    }

    @Override
    public void startMatch(Match match) {
        throw new IllegalStateException("Match is already in progress");
    }

    @Override
    public boolean canPauseMatch(Match match) {
        // Can pause if current innings is in progress and not completed
        return match.getCurrentInnings() != null &&
               !match.getCurrentInnings().isCompleted() &&
               match.getCurrentInnings().canContinue();
    }

    @Override
    public void pauseMatch(Match match) {
        if (!canPauseMatch(match)) {
            throw new IllegalStateException("Cannot pause match: no active innings or innings completed");
        }

        log.info("Pausing match: {}", match.getMatchName());
        match.setStatus(Match.MatchStatus.PAUSED);
    }

    @Override
    public boolean canResumeMatch(Match match) {
        return false; // This state handles pausing, not resuming
    }

    @Override
    public void resumeMatch(Match match) {
        throw new IllegalStateException("Cannot resume from IN_PROGRESS state - use PAUSED state");
    }

    @Override
    public boolean canEndMatch(Match match) {
        // Can end if all innings are completed or a result is clear
        return isMatchComplete(match);
    }

    @Override
    public void endMatch(Match match) {
        if (!canEndMatch(match)) {
            throw new IllegalStateException("Cannot end match: not all innings completed");
        }

        log.info("Completing match: {}", match.getMatchName());
        match.setStatus(Match.MatchStatus.COMPLETED);
        match.setEndTime(LocalDateTime.now());
        determineWinner(match);
    }

    @Override
    public boolean canAbandonMatch(Match match) {
        return true; // Can always abandon an in-progress match (weather, etc.)
    }

    @Override
    public void abandonMatch(Match match) {
        log.warn("Abandoning in-progress match: {}", match.getMatchName());
        match.setStatus(Match.MatchStatus.ABANDONED);
        match.setEndTime(LocalDateTime.now());
    }

    @Override
    public boolean canStartNewInnings(Match match) {
        // Can start new innings based on format rules
        if (match.isLimitedOvers()) {
            // Limited overs: can start if innings count < max and current innings completed
            return match.getCurrentInningsNumber() < match.getMaxInnings() &&
                   (match.getCurrentInnings() == null || match.getCurrentInnings().isCompleted());
        } else {
            // Test cricket: more complex rules
            return match.getCurrentInningsNumber() < match.getMaxInnings() &&
                   (match.getCurrentInnings() == null ||
                    match.getCurrentInnings().isCompleted() ||
                    match.getCurrentInnings().isDeclared());
        }
    }

    @Override
    public void handleInningsCompletion(Match match) {
        log.info("Handling innings completion for match: {}", match.getMatchName());

        if (match.getCurrentInnings() != null && match.getCurrentInnings().isCompleted()) {
            // Add to innings list if not already there
            if (match.getInnings() == null) {
                match.getInnings().add(match.getCurrentInnings());
            }

            // Check if match should end
            if (isMatchComplete(match)) {
                endMatch(match);
            } else if (canStartNewInnings(match)) {
                // Prepare for next innings
                prepareNextInnings(match);
            }
        }
    }

    private boolean isMatchComplete(Match match) {
        if (match.getFormat() == MatchFormat.TEST) {
            // Test cricket: complex completion logic
            return isTestMatchComplete(match);
        } else {
            // Limited overs: simpler logic
            return isLimitedOversMatchComplete(match);
        }
    }

    private boolean isLimitedOversMatchComplete(Match match) {
        // Match complete if both teams have batted or all overs completed
        boolean bothTeamsBatted = match.getCurrentInningsNumber() >= match.getMaxInnings();
        boolean allOversCompleted = match.getCurrentInnings() != null &&
                                   match.getCurrentInnings().getTotalOvers() >= match.getMaxOversPerInnings();

        // Or if a team wins by reaching target early
        boolean targetReached = isTargetReached(match);

        return bothTeamsBatted || allOversCompleted || targetReached;
    }

    private boolean isTestMatchComplete(Match match) {
        // Test cricket completion logic (simplified)
        boolean allInningsCompleted = match.getCurrentInningsNumber() >= match.getMaxInnings();
        boolean resultDecided = isTestResultDecided(match);

        return allInningsCompleted || resultDecided;
    }

    private boolean isTargetReached(Match match) {
        // In limited overs, if chasing team reaches target
        if (match.getCurrentInningsNumber() == 2 && match.getInnings() != null && match.getInnings().size() >= 2) {
            int firstInningsScore = match.getInnings().get(0).getTotalRuns();
            int secondInningsScore = match.getInnings().get(1).getTotalRuns();
            return secondInningsScore > firstInningsScore;
        }
        return false;
    }

    private boolean isTestResultDecided(Match match) {
        // Simplified: if team batting last has more runs than opponent can make
        if (match.getCurrentInningsNumber() >= 3 && match.getInnings() != null && match.getInnings().size() >= 2) {
            int lastBattingTeamScore = match.getInnings().get(match.getInnings().size() - 1).getTotalRuns();
            int opponentTotalScore = match.getInnings().stream()
                    .limit(match.getInnings().size() - 1)
                    .mapToInt(Innings::getTotalRuns)
                    .sum();

            return lastBattingTeamScore > opponentTotalScore;
        }
        return false;
    }

    private void prepareNextInnings(Match match) {
        match.setCurrentInningsNumber(match.getCurrentInningsNumber() + 1);

        // Update innings flags
        if (match.getCurrentInningsNumber() == 1) {
            match.setIsFirstInnings(true);
            match.setIsSecondInnings(false);
        } else if (match.getCurrentInningsNumber() == 2) {
            match.setIsFirstInnings(false);
            match.setIsSecondInnings(true);
        } else {
            match.setIsFirstInnings(false);
            match.setIsSecondInnings(false);
        }

        log.info("Prepared for innings {} in match: {}", match.getCurrentInningsNumber(), match.getMatchName());
    }

    private void determineWinner(Match match) {
        if (match.getInnings() == null || match.getInnings().isEmpty()) {
            log.warn("Cannot determine winner: no innings data");
            return;
        }

        // Simple winner determination logic
        if (match.getInnings().size() >= 2) {
            int team1Score = match.getInnings().get(0).getTotalRuns();
            int team2Score = match.getInnings().get(1).getTotalRuns();

            if (team1Score > team2Score) {
                match.setWinner(match.getTeam1());
                log.info("Match winner: {}", match.getTeam1().getName());
            } else if (team2Score > team1Score) {
                match.setWinner(match.getTeam2());
                log.info("Match winner: {}", match.getTeam2().getName());
            } else {
                log.info("Match ended in a tie");
            }
        }
    }
}
