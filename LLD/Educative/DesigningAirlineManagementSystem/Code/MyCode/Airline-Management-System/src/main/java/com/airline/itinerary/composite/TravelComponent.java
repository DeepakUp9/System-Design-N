package com.airline.itinerary.composite;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The Component Interface.
 * Defines the contract for both Leaf (Segment) and Composite (Itinerary).
 */
public interface TravelComponent {
    String getSource();
    String getDestination();
    BigDecimal getPrice();
    long getDurationInMinutes();
    LocalDateTime getDepartureTime();
    LocalDateTime getArrivalTime();
    void displayDetails();
}