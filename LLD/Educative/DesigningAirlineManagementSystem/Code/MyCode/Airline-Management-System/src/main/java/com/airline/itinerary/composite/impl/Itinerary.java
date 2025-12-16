package com.airline.itinerary.composite.impl;

import com.airline.itinerary.composite.TravelComponent;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Itinerary implements TravelComponent {
    private final List<TravelComponent> components = new ArrayList<>();

    public void addComponent(TravelComponent component) {
        components.add(component);
    }

    @Override
    public String getSource() {
        return components.isEmpty() ? "N/A" : components.get(0).getSource();
    }

    @Override
    public String getDestination() {
        return components.isEmpty() ? "N/A" : components.get(components.size() - 1).getDestination();
    }

    @Override
    public BigDecimal getPrice() {
        return components.stream()
                .map(TravelComponent::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public long getDurationInMinutes() {
        // Includes layovers between components
        long totalMinutes = 0;

        for (int i = 0; i < components.size(); i++) {
            TravelComponent current = components.get(i);
            totalMinutes += current.getDurationInMinutes(); // Flight time

            // Add layover to next segment (if exists)
            if (i < components.size() - 1) {
                TravelComponent next = components.get(i + 1);
                long layoverMinutes = Duration.between(
                        current.getArrivalTime(),
                        next.getDepartureTime()
                ).toMinutes();

                if (layoverMinutes > 0) {
                    totalMinutes += layoverMinutes;
                }
            }
        }

        return totalMinutes;
    }


    @Override
    public LocalDateTime getDepartureTime() {
        return components.get(0).getDepartureTime();
    }

    @Override
    public LocalDateTime getArrivalTime() {
        return components.get(components.size() - 1).getArrivalTime();
    }

    @Override
    public void displayDetails() {
        System.out.println("Full Itinerary from " + getSource() + " to " + getDestination());
        components.forEach(TravelComponent::displayDetails);
    }
}