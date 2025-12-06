package com.hms.hotel.domain.manager;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LLD: Singleton Pattern - Eager Initialization Implementation
 * Ensures only one instance of the Inventory Manager exists across the JVM.
 * This instance centrally manages limited resources.
 */
public final class InventoryManager {

    private static final Logger log = LoggerFactory.getLogger(InventoryManager.class);

    // 1. Eagerly create the single instance (Thread-safe initialization)
    private static final InventoryManager INSTANCE = new InventoryManager();

    // The core resource map (for simulation)
    private final Map<String, Integer> availableAssets;

    // 2. Private constructor prevents instantiation from outside
    private InventoryManager() {
        log.info("Singleton: Initializing InventoryManager for the first and only time.");
        this.availableAssets = new HashMap<>();

        // Load initial inventory (simulated)
        availableAssets.put("PARKING_SPOT", 50);
        availableAssets.put("BREAKFAST_VOUCHER", 100);
    }

    // 3. Public static method provides the global access point
    public static InventoryManager getInstance() {
        return INSTANCE;
    }

    // --- Singleton Business Methods ---

    /**
     * Attempts to reserve a specific quantity of an asset.
     * @return true if reservation succeeds, false otherwise (not enough inventory).
     */
    public synchronized boolean reserveAsset(String assetName, int quantity) {
        if (quantity <= 0) return true; // No reservation needed

        Integer currentCount = availableAssets.getOrDefault(assetName, 0);

        // Edge Case: Check for sufficient inventory (Thread-safe check due to 'synchronized')
        if (currentCount >= quantity) {
            availableAssets.put(assetName, currentCount - quantity);
            log.info("Inventory reserved: {} units of {}", quantity, assetName);
            return true;
        }

        log.warn("Inventory failed: Not enough {} available. Requested: {}, Available: {}",
                assetName, quantity, currentCount);
        return false;
    }

    /**
     * Releases an asset back into the inventory.
     */
    public synchronized void releaseAsset(String assetName, int quantity) {
        Integer currentCount = availableAssets.getOrDefault(assetName, 0);
        availableAssets.put(assetName, currentCount + quantity);
        log.info("Inventory released: {} units of {}", quantity, assetName);
    }

    public int getAvailableCount(String assetName) {
        return availableAssets.getOrDefault(assetName, 0);
    }
}