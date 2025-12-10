package com.stackclonell.stackclone.core.strategy;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
/**
 * LLD & Spring Integration: Factory to retrieve the correct Strategy object
 * based on a runtime identifier (e.g., query parameter "sort=votes").
 */
@Component
public class QuestionSortingStrategyFactory {

    private final Map<String, QuestionSortingStrategy> strategyMap;

    /**
     * Spring DI collects all QuestionSortingStrategy implementations.
     */
    public QuestionSortingStrategyFactory(List<QuestionSortingStrategy> strategies) {
        // Maps the list of strategies to a Map where the key is the strategyName()
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        QuestionSortingStrategy::getStrategyName,
                        strategy -> strategy
                ));
        System.out.println("LLD: QuestionSortingStrategyFactory initialized with strategies: " + this.strategyMap.keySet());
    }

    /**
     * Public method to retrieve the Strategy object.
     * @param strategyName The identifier (e.g., "votes", "newest")
     * @return The specific sorting strategy.
     */
    public QuestionSortingStrategy getStrategy(String strategyName) {
        // Edge Case: If the name is null or empty, default to 'newest'
        String key = (strategyName == null || strategyName.trim().isEmpty()) ? "newest" : strategyName.toLowerCase();

        QuestionSortingStrategy strategy = strategyMap.get(key);

        // Resilience: Fallback to the default if an unknown strategy is requested.
        if (strategy == null) {
            System.out.println("Warning: Unknown sorting strategy '" + key + "'. Falling back to 'newest'.");
            return strategyMap.get("newest");
        }
        return strategy;
    }
}