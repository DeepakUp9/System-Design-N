package com.hms.hotel.service;

import com.hms.hotel.domain.rate.BookingDetails;
import com.hms.hotel.domain.rate.IRateStrategy;
import com.hms.hotel.repository.RoomRepository; // Used to fetch base price
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * LLD: Strategy Pattern - Context (Spring Service)
 * Selects and executes the appropriate pricing strategy dynamically.
 */
@Service
public class RateCalculationService {

    private final RoomRepository roomRepository;

    /**
     * Map to hold all IRateStrategy beans, keyed by their Strategy Name.
     * This is the LLD-compliant way to manage strategies using Spring DI.
     */
    private final Map<String, IRateStrategy> strategyMap;

    /**
     * Spring DI collects all implementations of IRateStrategy and injects them into a List.
     * We then convert this List into a Map for easy runtime lookup.
     */
    public RateCalculationService(
            RoomRepository roomRepository,
            List<IRateStrategy> strategies) {

        this.roomRepository = roomRepository;
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(IRateStrategy::getStrategyName, Function.identity()));
    }

    /**
     * The main public method to calculate the final booking price.
     * @param roomNumber The room whose base price is needed.
     * @param details The booking details to evaluate against different strategies.
     * @return The final total price for the booking.
     */
    public BigDecimal calculateTotalPrice(String roomNumber, BookingDetails details) {
        // 1. Fetch Base Price
        BigDecimal basePrice = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber))
                .getBasePrice();

        // 2. Select the Best Strategy (Core LLD Logic)
        IRateStrategy selectedStrategy = selectBestStrategy(details);

        // 3. Execute the Strategy
        BigDecimal ratePerNight = selectedStrategy.calculateRate(basePrice, details);

        // Final calculation
        return ratePerNight.multiply(BigDecimal.valueOf(details.numberOfNights()));
    }

    /**
     * LLD Edge Case: Strategy Selection Logic
     * In a real HMS, this logic would be much more complex (e.g., comparing multiple calculated rates).
     * For now, we prioritize Loyalty over Standard.
     */
    private IRateStrategy selectBestStrategy(BookingDetails details) {
        if (details.isLoyaltyMember()) {
            return strategyMap.get("LOYALTY");
        }
        // Fallback to the default strategy
        return strategyMap.get("STANDARD");
    }
}