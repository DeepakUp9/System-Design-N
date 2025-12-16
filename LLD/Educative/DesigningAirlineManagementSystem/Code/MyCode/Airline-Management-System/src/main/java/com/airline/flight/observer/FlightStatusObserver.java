package com.airline.flight.observer;

import com.airline.flight.domain.Flight;

public interface FlightStatusObserver {
    void onStatusChange(Flight flight);
}