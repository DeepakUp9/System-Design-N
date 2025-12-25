package com.espn.cricinfo.api.controller;

import com.espn.cricinfo.api.dto.MatchCreateRequest;
import com.espn.cricinfo.api.dto.MatchResponse;
import com.espn.cricinfo.api.dto.MatchUpdateRequest;
import com.espn.cricinfo.infrastructure.dto.MatchDto;
import com.espn.cricinfo.infrastructure.service.MatchDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for cricket match management.
 * Provides endpoints for creating, reading, updating, and managing cricket matches.
 */
@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Cricket match management endpoints")
public class MatchController {

    private final MatchDataService matchDataService;

    /**
     * Create a new cricket match.
     */
    @PostMapping
    @Operation(summary = "Create a new cricket match")
    public ResponseEntity<MatchResponse> createMatch(
            @Valid @RequestBody MatchCreateRequest request) {

        var match = matchDataService.createMatch(
                request.matchName(),
                request.format(),
                request.team1Id(),
                request.team2Id(),
                request.venueId(),
                request.tournamentId(),
                request.startTime()
        );

        var response = MatchResponse.fromEntity(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get match by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get match details by ID")
    public ResponseEntity<MatchResponse> getMatch(
            @Parameter(description = "Match ID") @PathVariable Long id) {

        var match = matchDataService.getMatchDetails(id);
        if (match.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var response = MatchResponse.fromEntity(match.get());
        return ResponseEntity.ok(response);
    }

    /**
     * Update match details.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update match details")
    public ResponseEntity<MatchResponse> updateMatch(
            @Parameter(description = "Match ID") @PathVariable Long id,
            @Valid @RequestBody MatchUpdateRequest request) {

        // Implementation would update match details
        return ResponseEntity.ok().build();
    }

    /**
     * Start a match.
     */
    @PostMapping("/{id}/start")
    @Operation(summary = "Start a cricket match")
    public ResponseEntity<Map<String, String>> startMatch(
            @Parameter(description = "Match ID") @PathVariable Long id) {

        var innings = matchDataService.startMatch(id);
        return ResponseEntity.ok(Map.of(
                "message", "Match started successfully",
                "inningsId", innings.getId().toString()
        ));
    }

    /**
     * End a match.
     */
    @PostMapping("/{id}/end")
    @Operation(summary = "End a cricket match")
    public ResponseEntity<Map<String, String>> endMatch(
            @Parameter(description = "Match ID") @PathVariable Long id,
            @RequestParam Long winnerTeamId,
            @RequestParam String resultDescription) {

        var match = matchDataService.endMatch(id, winnerTeamId, resultDescription);
        return ResponseEntity.ok(Map.of(
                "message", "Match ended successfully",
                "winner", match.getWinner() != null ? match.getWinner().getName() : "Draw"
        ));
    }

    /**
     * Get live matches.
     */
    @GetMapping("/live")
    @Operation(summary = "Get all live matches")
    public ResponseEntity<List<MatchResponse>> getLiveMatches() {
        var liveMatches = matchDataService.getLiveMatches();
        var responses = liveMatches.stream()
                .map(MatchResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Search matches with filters.
     */
    @GetMapping("/search")
    @Operation(summary = "Search matches with filters")
    public ResponseEntity<Page<MatchResponse>> searchMatches(
            @RequestParam(required = false) String teamName,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            Pageable pageable) {

        var matches = matchDataService.searchMatches(teamName, null, startDate, endDate, pageable);
        return ResponseEntity.ok(matches.map(MatchResponse::fromEntity));
    }

    /**
     * Get match analytics.
     */
    @GetMapping("/{id}/analytics")
    @Operation(summary = "Get match analytics and statistics")
    public ResponseEntity<Map<String, Object>> getMatchAnalytics(
            @Parameter(description = "Match ID") @PathVariable Long id) {

        var analytics = matchDataService.getMatchAnalytics(id);
        return ResponseEntity.ok(analytics);
    }

    /**
     * Pause a match.
     */
    @PostMapping("/{id}/pause")
    @Operation(summary = "Pause a match")
    public ResponseEntity<Map<String, String>> pauseMatch(
            @Parameter(description = "Match ID") @PathVariable Long id) {

        // Implementation would pause the match
        return ResponseEntity.ok(Map.of("message", "Match paused"));
    }

    /**
     * Resume a match.
     */
    @PostMapping("/{id}/resume")
    @Operation(summary = "Resume a paused match")
    public ResponseEntity<Map<String, String>> resumeMatch(
            @Parameter(description = "Match ID") @PathVariable Long id) {

        // Implementation would resume the match
        return ResponseEntity.ok(Map.of("message", "Match resumed"));
    }

    /**
     * Abandon a match.
     */
    @PostMapping("/{id}/abandon")
    @Operation(summary = "Abandon a match")
    public ResponseEntity<Map<String, String>> abandonMatch(
            @Parameter(description = "Match ID") @PathVariable Long id) {

        // Implementation would abandon the match
        return ResponseEntity.ok(Map.of("message", "Match abandoned"));
    }

    /**
     * Get matches by tournament.
     */
    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Get matches for a tournament")
    public ResponseEntity<List<MatchResponse>> getMatchesByTournament(
            @Parameter(description = "Tournament ID") @PathVariable Long tournamentId) {

        // Implementation would get matches for tournament
        return ResponseEntity.ok(List.of());
    }

    /**
     * Get matches by venue.
     */
    @GetMapping("/venue/{venueId}")
    @Operation(summary = "Get matches at a venue")
    public ResponseEntity<List<MatchResponse>> getMatchesByVenue(
            @Parameter(description = "Venue ID") @PathVariable Long venueId) {

        // Implementation would get matches at venue
        return ResponseEntity.ok(List.of());
    }
}
