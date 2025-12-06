package com.hms.hotel.service;

import com.hms.hotel.domain.manager.InventoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

    private static final Logger log = LoggerFactory.getLogger(AssetService.class);
    // Access the Singleton instance globally
    private final InventoryManager inventoryManager = InventoryManager.getInstance();

    public boolean allocateParking(String bookingRef, int spots) {
        log.info("Attempting to allocate {} parking spots for booking {}", spots, bookingRef);

        // LLD: Using the Singleton for centralized resource control
        boolean success = inventoryManager.reserveAsset("PARKING_SPOT", spots);

        if (success) {
            log.info("Parking allocated successfully.");
        } else {
            // Edge Case: Booking might need to be rejected or deferred if critical inventory fails
            log.error("Parking allocation failed. Critical inventory shortage.");
        }
        return success;
    }

    public void releaseAssets(String bookingRef, int spots) {
        log.info("Releasing assets for booking {}", bookingRef);
        inventoryManager.releaseAsset("PARKING_SPOT", spots);
    }
}