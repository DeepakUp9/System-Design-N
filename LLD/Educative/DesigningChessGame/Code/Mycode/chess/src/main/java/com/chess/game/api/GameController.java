package com.chess.game.api;


import com.chess.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST CONTROLLER: Exposes the GameService via a REST API.
 * This is the framework's job: handling I/O, not business logic.
 */
@RestController
@RequestMapping("/api/chess")
public class GameController {

    private final GameService gameService;

    // Dependency Injection (DI) is handled by Spring
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * POST /api/chess/start
     * Starts a new game.
     */
    @PostMapping("/start")
    public ResponseEntity<String> startGame() {
        gameService.createNewGame();
        return ResponseEntity.ok("New game started successfully. White to move.");
    }

    /**
     * POST /api/chess/move
     * Executes a move: e.g., POST with { "sR": 1, "sC": 4, "eR": 3, "eC": 4 }
     */
    @PostMapping("/move")
    public ResponseEntity<String> makeMove(@RequestBody MoveRequest request) {
        String result = gameService.makeMove(
                request.sR, request.sC,
                request.eR, request.eC
        );
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/chess/status
     * Returns the current board state and active player.
     */
    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        if (gameService.getCurrentGameContext() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not started.");
        }
        // NOTE: In a production app, we would map the GameContext to a cleaner DTO.
        return ResponseEntity.ok(gameService.getCurrentGameContext());
    }

    // Inner DTO for receiving move requests
    private static class MoveRequest {
        public int sR; // startRow
        public int sC; // startCol
        public int eR; // endRow
        public int eC; // endCol
    }
}