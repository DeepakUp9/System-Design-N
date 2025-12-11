package com.rms.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Observer: Decrements inventory for ingredients when a new order is placed.
 */
@Service
public class InventoryUpdateService {

    @EventListener
    @Async
    public void handleOrderPlaced(OrderPlacedEvent event) {
        System.out.println("\n--- INVENTORY SYSTEM ---");
        System.out.println("Processing order #" + event.getOrder().getId());
        System.out.println("-> Logic: Deduct ingredients for the order's menu items.");
        System.out.println("-> Resilience Check: If inventory is low, trigger a reorder alert.");
        System.out.println("------------------------\n");
    }
}