package com.rms.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Observer: Updates the Kitchen Display System (KDS) when a new order is placed.
 */
@Service
public class KitchenDisplayService {

    @EventListener
    @Async // Makes the notification non-blocking (crucial for resilience/scalability)
    public void handleOrderPlaced(OrderPlacedEvent event) {
        // The event listener is activated ONLY when an OrderPlacedEvent is published.

        System.out.println("\n*** KITCHEN DISPLAY ***");
        System.out.println("RECEIVED NEW ORDER: #" + event.getOrder().getId());
        System.out.println("Channel: " + event.getOrder().getChannelType());
        System.out.println("-> Logic: Convert Order to a Kitchen Ticket format and display it.");
        System.out.println("***********************\n");
    }
}