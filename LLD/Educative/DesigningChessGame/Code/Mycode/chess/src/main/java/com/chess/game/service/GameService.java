package com.chess.game.service;


import com.chess.game.core.model.Color;
import com.chess.game.core.model.Move;
import com.chess.game.core.model.Piece;
import com.chess.game.core.model.Position;
import com.chess.game.core.factory.PieceFactory;
import com.chess.game.core.util.Board;
import com.chess.game.persistence.entity.GameEntity;
import com.chess.game.core.state.GameContext;
import com.chess.game.persistence.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

/**
 * SPRING SERVICE: The entry point for the business logic.
 * It manages the lifecycle of the LLD core object (GameContext) and
 * provides methods for the Controller to call.
 */
/**
 * REFAC: GameService now manages the transaction boundary and uses the Repository.
 */
/**
 * SPRING SERVICE: The entry point for the business logic.
 * It manages the lifecycle of the LLD core object (GameContext) and
 * provides methods for the Controller to call.
 */
@Service
public class GameService {

    // NOTE: This should eventually be stored in PostgreSQL (JPA/Hibernate),
    // but for now, we hold it in memory for simple LLD demonstration.
    // We now use an ID to track the current game, not an in-memory object.
    private UUID activeGameId;
    private final GameRepository gameRepository;
    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    /**
     * Initializes a new game instance.
     */
    public UUID createNewGame() {
        Board board = new Board();

        // Use the Factory to populate the board
        Piece[][] initialSetup = PieceFactory.getInitialPiecePlacement();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (initialSetup[r][c] != null) {
                    board.setPiece(new Position(r, c), initialSetup[r][c]);
                }
            }
        }

        // 1. Create the persistent entity
        GameEntity newEntity = GameEntity.createNew(board);

        // 2. Save and set the active ID
        newEntity = gameRepository.save(newEntity);
        this.activeGameId = newEntity.getId();

        return this.activeGameId;
    }

    /**
     * Handles a player's move. This is the only method that interacts with the
     * State Pattern logic.
     *
     * @param startRow, startCol The starting position.
     * @param endRow, endCol The ending position.
     * @return The new game status/state name.
     */
    public String makeMove(int startRow, int startCol, int endRow, int endCol) {
        // 1. LOAD: Fetch the entity from the database
        GameEntity entity = gameRepository.findById(activeGameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + activeGameId));

        // 2. RECONSTRUCT CONTEXT: Restore the LLD core object from the persisted data
        // NOTE: A proper implementation would need a mechanism to map gameStateName back to a ConcreteGameState object (e.g., using another Factory).
        // For simplicity, we create a temporary GameContext and set the board/player data.
        GameContext gameContext = new GameContext(entity.getBoard());
        gameContext.switchActivePlayer(); // Resetting to White, then switching to Black if entity.activePlayer is Black
        if (entity.getActivePlayer() == Color.BLACK) {
            gameContext.switchActivePlayer();
        }

        // 3. EXECUTE MOVE (LLD core logic)
        Move move = new Move(new Position(startRow, startCol), new Position(endRow, endCol));

        try {
            gameContext.submitMove(move);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Move: " + e.getMessage());
        }

        // 4. PERSIST: Update the entity with the new state from the LLD context
        entity.setBoard(gameContext.getBoard());
        entity.setActivePlayer(gameContext.getActivePlayer());
        entity.setGameStateName(gameContext.getCurrentState().getClass().getSimpleName());

        // 5. SAVE: The save is done within the @Transactional boundary
        gameRepository.save(entity);

        return "Move successful. It is now " + gameContext.getActivePlayer() + "'s turn. Status: " + gameContext.getCurrentState().getStatus();
    }

    /**
     * Get the current state of the board and game.
     */
    public GameContext getCurrentGameContext() {
        GameEntity entity = gameRepository.findById(activeGameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + activeGameId));

        // In a real system, you would fully reconstruct the GameContext and State object here.
        GameContext context = new GameContext(entity.getBoard());
        // ... set state and active player based on entity ...
        return context;
    }
    public UUID getActiveGameId() {
        return activeGameId;
    }
}