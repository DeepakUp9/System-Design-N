package com.rms.application.factory;

import com.rms.core.strategy.FulfillmentStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test: Ensures Spring correctly wires all FulfillmentStrategy beans
 * into the Factory's Map.
 */
@SpringBootTest
// Use 'test' profile if needed for separate test configuration
@ActiveProfiles("test")
public class FulfillmentStrategyFactoryTest {

    @Autowired
    private FulfillmentStrategyFactory factory;

    @Test
    void testGetStrategy_DineInStrategy() {
        // ACT: Request a specific strategy type
        FulfillmentStrategy strategy = factory.getStrategy("DINE_IN");

        // ASSERT: Verify the correct concrete implementation is returned
        assertNotNull(strategy);
        assertEquals("DINE_IN", strategy.getChannelType());
        assertTrue(strategy.getClass().getSimpleName().contains("DineIn"));
    }

    @Test
    void testGetStrategy_OnlineDeliveryStrategy() {
        // ACT: Request a specific strategy type
        FulfillmentStrategy strategy = factory.getStrategy("ONLINE_DELIVERY");

        // ASSERT: Verify the correct concrete implementation is returned
        assertNotNull(strategy);
        assertEquals("ONLINE_DELIVERY", strategy.getChannelType());
        assertTrue(strategy.getClass().getSimpleName().contains("OnlineDelivery"));
    }

    @Test
    void testGetStrategy_InvalidType_ThrowsException() {
        // ASSERT: Edge case handling for unknown types
        assertThrows(IllegalArgumentException.class, () -> {
            factory.getStrategy("DRIVE_THRU_FUTURE");
        });
    }
}