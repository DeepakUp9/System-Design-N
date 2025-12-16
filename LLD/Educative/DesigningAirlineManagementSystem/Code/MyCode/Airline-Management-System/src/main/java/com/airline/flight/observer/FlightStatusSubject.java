package com.airline.flight.observer;

import com.airline.flight.domain.Flight;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FlightStatusSubject {
    private final List<FlightStatusObserver> observers = new ArrayList<>();

    public void attach(FlightStatusObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(Flight flight) {
        observers.forEach(observer -> observer.onStatusChange(flight));
    }
}