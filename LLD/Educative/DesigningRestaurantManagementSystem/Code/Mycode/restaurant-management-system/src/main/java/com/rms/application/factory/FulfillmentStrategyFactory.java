package com.rms.application.factory;

import com.rms.core.strategy.FulfillmentStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory for retrieving the correct FulfillmentStrategy based on the channel type.
 * This class uses Spring's ability to inject all implementations of an interface into a Map.
 */
@Service
public class FulfillmentStrategyFactory {

    // Map: Key = ChannelType (String), Value = FulfillmentStrategy implementation
    private final Map<String, FulfillmentStrategy> strategyMap;

    // Spring automatically finds all beans implementing FulfillmentStrategy
    // and injects them as a List. We convert this List into a Map for O(1) lookup.
    public FulfillmentStrategyFactory(List<FulfillmentStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        FulfillmentStrategy::getChannelType,
                        Function.identity()
                ));
    }

    /**
     * Retrieves the specific strategy for a given channel type.
     * @param channelType e.g., "ONLINE_DELIVERY", "DINE_IN"
     * @return The corresponding FulfillmentStrategy implementation.
     * @throws IllegalArgumentException if no strategy is found (edge case).
     */
    public FulfillmentStrategy getStrategy(String channelType) {
        FulfillmentStrategy strategy = strategyMap.get(channelType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown fulfillment channel type: " + channelType);
        }
        return strategy;
    }
}