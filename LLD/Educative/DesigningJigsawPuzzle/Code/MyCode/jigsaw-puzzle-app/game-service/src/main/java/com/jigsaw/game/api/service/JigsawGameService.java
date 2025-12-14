package com.jigsaw.game.api.service;

import com.jigsaw.game.api.entity.JigsawGameEntity;
import com.jigsaw.game.api.event.GamePiecePlacedEvent;
import com.jigsaw.game.api.event.GameSolvedEvent;
import com.jigsaw.game.api.publisher.GameEventPublisher;
import com.jigsaw.game.api.repository.GameRepository;
import com.jigsaw.game.model.*;
import com.jigsaw.game.state.GameState;
import com.jigsaw.game.state.SetupState;
import com.jigsaw.game.state.SolvedState;
import com.jigsaw.game.strategy.PieceMatchStrategy;
import com.jigsaw.game.api.dto.GameMoveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The Application Service Layer. UPDATED to include persistence logic.
 */
@Service
@RequiredArgsConstructor
public class JigsawGameService {

    private final Map<PieceType, PieceMatchStrategy> pieceStrategyMap;
    private final GameRepository gameRepository; // Real Repository injected
    private final GameEventPublisher eventPublisher;

    /**
     * Helper method to convert the State string back to the concrete GameState object.
     * In a production system, this is managed by a factory or Spring bean map.
     */
    private GameState getStateFromEntity(String stateName) {
        // Simple mapping, must be expanded in production
        return switch (stateName) {
            case "InProgressState" -> new com.jigsaw.game.state.InProgressState();
            case "SolvedState" -> new com.jigsaw.game.state.SolvedState();
            default -> new SetupState();
        };
    }

    /**
     * Fetches game entity, maps it to LLD Domain, and re-hydrates the Strategy and State.
     * @Cacheable: If the game is found in 'game-state-cache', return it immediately.
     */
    @Cacheable(value = "game-state-cache", key = "#gameId")
    @Transactional(readOnly = true)
    public JigsawGame getGameById(UUID gameId) {
        JigsawGameEntity entity = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found with ID: " + gameId));

        // 1. Re-hydrate the LLD State Context
        GameState restoredState = getStateFromEntity(entity.getCurrentState());
        JigsawGame domainGame = entity.toDomain(restoredState);

        // 2. Re-hydrate the LLD Strategy Contexts (all PuzzlePieces)
        domainGame.getPieces().forEach(piece -> {
            PieceMatchStrategy strategy = pieceStrategyMap.get(piece.getType());
            if (strategy != null) {
                piece.setMatchingStrategy(strategy);
            } else {
                throw new IllegalStateException("Missing Strategy for PieceType: " + piece.getType());
            }
        });

        return domainGame;
    }


    /**
     * Creates a new game instance.
     */
    @Transactional
    public JigsawGame createNewGame(List<PuzzlePiece> pieces, List<Player> players) {
        // 1. Initialization and Strategy Assignment (from old method)
        pieces.forEach(piece -> piece.setMatchingStrategy(pieceStrategyMap.get(piece.getType())));

        // 2. Create the LLD State Context
        JigsawGame newGame = JigsawGame.builder()
                .boardWidth(10)
                .boardHeight(10)
                .pieces(pieces)
                .players(players)
                .build();

        // 3. Set the initial LLD State
        newGame.changeState(new SetupState());

        // 4. Persistence: Convert LLD to JPA Entity and Save
        JigsawGameEntity entity = JigsawGameEntity.fromDomain(newGame);
        gameRepository.save(entity);

        return newGame;
    }

    /**
     * Handles a player's move, delegating to the GameState pattern and updating cache.
     * @Caching: Ensures the cache is updated (CachePut) or evicted (CacheEvict) after a write.
     */
    @Caching(put = {
            @CachePut(value = "game-state-cache", key = "#gameId") // Put the updated game into cache
    })
    @Transactional(rollbackFor = Exception.class)
    public boolean processGameMove(UUID gameId, GameMoveRequest moveRequest) {
        JigsawGame game = getGameById(gameId);
        BoardPosition position = new BoardPosition(moveRequest.row(), moveRequest.col());
        boolean success = game.placePiece(moveRequest.pieceId(), position);

        if (success) {
            JigsawGameEntity entityToSave = JigsawGameEntity.fromDomain(game);
            gameRepository.save(entityToSave);

            // LLD Observer Pattern: Publish the event for successful moves
            GamePiecePlacedEvent placedEvent = GamePiecePlacedEvent.create(
                    game.getGameId(),
                    moveRequest.pieceId(),
                    moveRequest.playerId(), // Assuming playerId is correct from the request
                    moveRequest.row(),
                    moveRequest.col()
            );
            eventPublisher.publishPiecePlacedEvent(placedEvent);

            // Check if the state transition occurred to SolvedState
            if (game.getCurrentState() instanceof SolvedState) {
                GameSolvedEvent solvedEvent = new GameSolvedEvent(
                        game.getGameId(),
                        moveRequest.playerId(), // Simplified: Last mover is winner
                        game.getCurrentState().getClass().getSimpleName(),
                        LocalDateTime.now()
                );
                eventPublisher.publishGameSolvedEvent(solvedEvent);
            }
        }

        return success;
    }

    // Dummy game creation is now removed, replaced by real persistence calls.
}