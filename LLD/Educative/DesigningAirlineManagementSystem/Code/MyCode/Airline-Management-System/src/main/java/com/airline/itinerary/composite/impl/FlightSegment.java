package com.airline.itinerary.composite.impl;

import com.airline.itinerary.composite.TravelComponent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class FlightSegment implements TravelComponent {
    private final String flightNumber;
    private final String source;
    private final String destination;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;
    private final BigDecimal price;

    @Override
    public long getDurationInMinutes() {
        return Duration.between(departureTime, arrivalTime).toMinutes();
    }

    @Override
    public void displayDetails() {
        System.out.println("Flight " + flightNumber + ": " + source + " -> " + destination);
    }
}