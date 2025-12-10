package com.amazon.catalogservice.infrastructure.messaging;

import com.amazon.catalogservice.domain.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

// --- DTO must be replicated or shared via a common module (Production LLD) ---
// For simplicity here, we assume the OrderPlacedEvent DTO is available.
// NOTE: This class will need the OrderPlacedEvent record from the other service.

@Component
public class InventoryUpdateListener {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public InventoryUpdateListener(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    /**
     * Simulates the message consumption endpoint (e.g., @KafkaListener, @JmsListener)
     */
    public void listenForOrderPlacedEvent(String messagePayload) {
        try {
            // 1. Deserialize the message (simulated here)
            // OrderPlacedEvent event = objectMapper.readValue(messagePayload, OrderPlacedEvent.class);

            // --- Simulated Message Broker Receive ---
            System.out.println("--- OBSERVER PATTERN: SUBSCRIBE ---");
            System.out.println("CATALOG SERVICE: Received event for inventory update.");

            // 2. Business Logic: Update inventory stock based on items in the order
            // event.items().forEach(item -> {
            //     // LLD: Delegate the actual business operation to the ProductService
            //     productService.reduceInventory(item.productId(), item.quantity());
            //     System.out.println("CATALOG SERVICE: Reserved " + item.quantity() + " for Product ID: " + item.productId());
            // });

            System.out.println("CATALOG SERVICE: Inventory update acknowledged successfully.");
            System.out.println("-------------------------------------");

        } catch (Exception e) {
            // Resilience: Log and handle message failures (e.g., parsing errors, retries)
            System.err.println("Error processing Order Placed event: " + e.getMessage());
        }
    }
}