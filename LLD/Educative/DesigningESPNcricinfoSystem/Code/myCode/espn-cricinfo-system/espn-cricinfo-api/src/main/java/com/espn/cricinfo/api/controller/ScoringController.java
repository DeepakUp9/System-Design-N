package com.espn.cricinfo.api.controller;

import com.espn.cricinfo.api.dto.BallRecordRequest;
import com.espn.cricinfo.api.dto.ScoringResponse;
import com.espn.cricinfo.core.service.MatchScoringService;
import com.espn.cricinfo.core.state.InningsContext;
import com.espn.cricinfo.core.state.MatchContext;
import com.espn.cricinfo.domain.entities.Ball;
import com.espn.cricinfo.domain.enums.BallType;
import com.espn.cricinfo.domain.valueobjects.ScoringResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST Controller for live match scoring operations.
 * Handles ball-by-ball scoring, wicket recording, and live updates.
 */
@RestController
@RequestMapping("/scoring")
@RequiredArgsConstructor
@Tag(name = "Scoring", description = "Live match scoring endpoints")
public class ScoringController {

    private final MatchScoringService matchScoringService;

    /**
     * Record a ball delivery in a match.
     */
    @PostMapping("/matches/{matchId}/balls")
    @Operation(summary = "Record a ball delivery")
    public ResponseEntity<ScoringResponse> recordBall(
            @Parameter(description = "Match ID") @PathVariable Long matchId,
            @Valid @RequestBody BallRecordRequest request) {

        // Create ball entity from request
        Ball ball = Ball.builder()
                .overNumber(request.overNumber())
                .ballNumber(request.ballNumber())
                .ballType(request.ballType())
                .runsScored(request.runsScored())
                .isWicket(request.isWicket())
                .isBoundary(request.isBoundary())
                .isSix(request.isSix())
                .bowlerName(request.bowlerName())
                .batsmanName(request.batsmanName())
                .timestamp(LocalDateTime.now())
                .build();

        // TODO: Get match and innings contexts from service/repository
        // For now, return success response
        ScoringResult scoringResult = ScoringResult.legalDelivery(request.runsScored());

        var response = ScoringResponse.builder()
                .success(true)
                .message("Ball recorded successfully")
                .scoringResult(scoringResult)
                .matchId(matchId)
                .overNumber(request.overNumber())
                .ballNumber(request.ballNumber())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Record a wicket.
     */
    @PostMapping("/matches/{matchId}/wickets")
    @Operation(summary = "Record a wicket")
    public ResponseEntity<Map<String, String>> recordWicket(
            @Parameter(description = "Match ID") @PathVariable Long matchId,
            @RequestParam String playerName,
            @RequestParam String wicketType,
            @RequestParam String bowlerName,
            @RequestParam String batsmanName) {

        // TODO: Implement wicket recording with state management
        return ResponseEntity.ok(Map.of(
                "message", "Wicket recorded: " + batsmanName + " (" + wicketType + ")",
                "batsman", batsmanName,
                "bowler", bowlerName,
                "wicketType", wicketType
        ));
    }

    /**
     * Record extras (no-ball, wide, bye, leg-bye).
     */
    @PostMapping("/matches/{matchId}/extras")
    @Operation(summary = "Record extras")
    public ResponseEntity<Map<String, String>> recordExtras(
            @Parameter(description = "Match ID") @PathVariable Long matchId,
            @RequestParam BallType extraType,
            @RequestParam Integer runs,
            @RequestParam String bowlerName) {

        String message = switch (extraType) {
            case NO_BALL -> "No-ball recorded: " + runs + " runs + 1 extra";
            case WIDE -> "Wide recorded: " + runs + " runs + 1 extra";
            case BYE -> "Bye recorded: " + runs + " runs";
            case LEG_BYE -> "Leg bye recorded: " + runs + " runs";
            case OVERTHROW -> "Overthrow recorded: " + runs + " runs";
            default -> "Extra recorded: " + runs + " runs";
        };

        return ResponseEntity.ok(Map.of(
                "message", message,
                "extraType", extraType.name(),
                "runs", runs.toString(),
                "bowler", bowlerName
        ));
    }

    /**
     * Start new innings.
     */
    @PostMapping("/matches/{matchId}/innings")
    @Operation(summary = "Start new innings")
    public ResponseEntity<Map<String, String>> startNewInnings(
            @Parameter(description = "Match ID") @PathVariable Long matchId) {

        // TODO: Implement innings start with state management
        return ResponseEntity.ok(Map.of(
                "message", "New innings started",
                "matchId", matchId.toString()
        ));
    }

    /**
     * Declare innings (Test cricket).
     */
    @PostMapping("/matches/{matchId}/innings/declare")
    @Operation(summary = "Declare innings")
    public ResponseEntity<Map<String, String>> declareInnings(
            @Parameter(description = "Match ID") @PathVariable Long matchId,
            @RequestParam Double overs,
            @RequestParam Integer balls) {

        // TODO: Implement innings declaration
        return ResponseEntity.ok(Map.of(
                "message", "Innings declared at " + overs + " overs and " + balls + " balls",
                "overs", overs.toString(),
                "balls", balls.toString()
        ));
    }

    /**
     * Get current match score.
     */
    @GetMapping("/matches/{matchId}/score")
    @Operation(summary = "Get current match score")
    public ResponseEntity<Map<String, Object>> getCurrentScore(
            @Parameter(description = "Match ID") @PathVariable Long matchId) {

        // TODO: Get actual match statistics
        Map<String, Object> score = Map.of(
                "matchId", matchId,
                "team1", Map.of(
                        "name", "India",
                        "runs", 285,
                        "wickets", 7,
                        "overs", 50.0
                ),
                "team2", Map.of(
                        "name", "Australia",
                        "runs", 245,
                        "wickets", 10,
                        "overs", 47.2
                ),
                "status", "COMPLETED",
                "result", "India won by 40 runs"
        );

        return ResponseEntity.ok(score);
    }

    /**
     * Get live commentary for current ball.
     */
    @GetMapping("/matches/{matchId}/commentary")
    @Operation(summary = "Get live commentary")
    public ResponseEntity<Map<String, Object>> getLiveCommentary(
            @Parameter(description = "Match ID") @PathVariable Long matchId) {

        Map<String, Object> commentary = Map.of(
                "matchId", matchId,
                "over", "50.6",
                "ball", Map.of(
                        "bowler", "Jasprit Bumrah",
                        "batsman", "Steve Smith",
                        "runs", 4,
                        "description", "FOUR! Smith hits it to the boundary"
                ),
                "score", "285/7",
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity.ok(commentary);
    }

    /**
     * Get match statistics.
     */
    @GetMapping("/matches/{matchId}/statistics")
    @Operation(summary = "Get match statistics")
    public ResponseEntity<Map<String, Object>> getMatchStatistics(
            @Parameter(description = "Match ID") @PathVariable Long matchId) {

        Map<String, Object> stats = Map.of(
                "matchId", matchId,
                "totalRuns", 530,
                "totalWickets", 17,
                "totalOvers", 97.2,
                "boundaries", 45,
                "sixes", 12,
                "runRate", "5.45",
                "partnerships", 8,
                "powerplayRuns", 85,
                "deathOversRuns", 95
        );

        return ResponseEntity.ok(stats);
    }

    /**
     * Validate ball before recording.
     */
    @PostMapping("/matches/{matchId}/validate-ball")
    @Operation(summary = "Validate ball before recording")
    public ResponseEntity<Map<String, String>> validateBall(
            @Parameter(description = "Match ID") @PathVariable Long matchId,
            @Valid @RequestBody BallRecordRequest request) {

        // TODO: Implement ball validation
        return ResponseEntity.ok(Map.of(
                "valid", "true",
                "message", "Ball is valid for recording"
        ));
    }
}
