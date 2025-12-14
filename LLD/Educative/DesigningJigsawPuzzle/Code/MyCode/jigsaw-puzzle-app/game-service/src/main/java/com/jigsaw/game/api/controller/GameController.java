package com.jigsaw.game.api.controller;

import com.jigsaw.game.api.dto.GameMoveRequest;
import com.jigsaw.game.api.dto.GameStatusResponse;
import com.jigsaw.game.api.service.JigsawGameService;
import com.jigsaw.game.model.JigsawGame;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The API Entry Point (Controller).
 * Handles HTTP specifics (mapping, request body, headers) and delegates to the Service.
 */
@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final JigsawGameService gameService;

    // --- TEMPORARY ENDPOINT FOR TESTING/SETUP ---
    // In a real system, piece creation and game creation would be separate complex operations.
    @PostMapping("/start")
    public ResponseEntity<GameStatusResponse> startGame() {
        // Placeholder for creating a dummy game for quick testing
        JigsawGame game = gameService.createDummyGame(UUID.randomUUID());
        return new ResponseEntity<>(
                GameStatusResponse.from(game, true, "New game started and entered InProgressState."),
                HttpStatus.CREATED
        );
    }
    // -------------------------------------------


    /**
     * Endpoint to allow a player to place or move a piece.
     * Uses the LLD State and Strategy patterns via the GameService.
     * @param gameId The UUID of the game instance.
     * @param request The move details (piece, position, player).
     * @return GameStatusResponse indicating success/failure.
     */
    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameStatusResponse> processMove(
            @PathVariable UUID gameId,
            @Valid @RequestBody GameMoveRequest request) { // @Valid triggers DTO validation

        try {
            // Note: In a real system, the game object would be fetched from the DB here.
            // For now, we use the service's dummy game logic.
            boolean success = gameService.processGameMove(gameId, request);

            String message = success ?
                    "Move successfully executed." :
                    "Move failed due to invalid placement or game state rules.";

            // Temporary: Fetch the updated state from the dummy game.
            // JigsawGame updatedGame = gameService.getGameById(gameId); // Real persistence call
            JigsawGame updatedGame = gameService.createDummyGame(gameId); // Placeholder

            if (success) {
                return ResponseEntity.ok(GameStatusResponse.from(updatedGame, true, message));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(GameStatusResponse.from(updatedGame, false, message));
            }

        } catch (Exception e) {
            // Production-level error handling (see Note below)
            System.err.println("API Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GameStatusResponse(gameId, "ERROR", false, "System Error: " + e.getMessage(), LocalDateTime.now()));
        }
    }
}