package com.jigsaw.game.api.config;

import com.jigsaw.game.model.PieceType;
import com.jigsaw.game.strategy.CornerPieceStrategy;
import com.jigsaw.game.strategy.EdgePieceStrategy;
import com.jigsaw.game.strategy.InternalPieceStrategy;
import com.jigsaw.game.strategy.PieceMatchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Production-Level LLD Integration: This configuration bridges Spring's DI
 * with our core Strategy Pattern implementation.
 * It initializes all concrete strategies as Spring singleton beans.
 */
@Configuration
public class StrategyConfiguration {

    // Define individual strategies as singleton beans
    @Bean
    public CornerPieceStrategy cornerPieceStrategy() {
        return new CornerPieceStrategy();
    }

    @Bean
    public EdgePieceStrategy edgePieceStrategy() {
        return new EdgePieceStrategy();
    }

    @Bean
    public InternalPieceStrategy internalPieceStrategy() {
        return new InternalPieceStrategy();
    }

    /**
     * CRITICAL LLD Factory Implementation using Spring DI.
     * This method collects all PieceMatchStrategy beans and maps them by piece type.
     * This avoids using large 'if-else' statements in the service layer (adhering to OCP).
     * @param strategies All PieceMatchStrategy beans injected by Spring.
     * @return A map that acts as the Strategy Factory/Locator.
     */
    @Bean
    public Map<PieceType, PieceMatchStrategy> pieceStrategyMap(
            CornerPieceStrategy cornerPieceStrategy,
            EdgePieceStrategy edgePieceStrategy,
            InternalPieceStrategy internalPieceStrategy) {

        // Production Note: The key mapping requires custom logic as strategies don't directly expose their type.
        // We manually map them here, which is standard practice for Strategy Pattern injection in Spring.
        return Map.of(
                PieceType.CORNER, cornerPieceStrategy,
                PieceType.EDGE, edgePieceStrategy,
                PieceType.INTERNAL, internalPieceStrategy
        );
    }
}