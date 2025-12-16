package com.airline.reservation.state.impl;

import com.airline.core.exception.BusinessException;
import com.airline.reservation.domain.Reservation;
import com.airline.reservation.domain.ReservationStatus;
import com.airline.reservation.state.ReservationState;
import org.springframework.http.HttpStatus;

public class BoardedState implements ReservationState {

    @Override
    public void confirm(Reservation context) {
        throw new BusinessException("Passenger already boarded.",
                "ERR_ALREADY_BOARDED", HttpStatus.CONFLICT);
    }

    @Override
    public void cancel(Reservation context) {
        // EXTREMELY RARE - but could happen:
        // 1. Medical emergency
        // 2. Security issue
        // 3. Offload request
        // Requires HIGH authorization
        if (!authorizedForOffload()) {
            throw new BusinessException("Cannot cancel after boarding. Requires supervisor approval.",
                    "ERR_CANNOT_CANCEL_BOARDED", HttpStatus.FORBIDDEN);
        }
        context.setStatus(ReservationStatus.CANCELLED);
        context.setCurrentState(new CancelledState());
    }

    @Override
    public void checkIn(Reservation context) {
        throw new BusinessException("Already boarded. Cannot check-in again.",
                "ERR_ALREADY_BOARDED", HttpStatus.CONFLICT);
    }

    @Override
    public void expire(Reservation context) {
        // Boarded passengers don't expire
        // They either fly or get offloaded
        throw new BusinessException("Boarded passengers do not expire.",
                "ERR_INVALID_TRANSITION", HttpStatus.BAD_REQUEST);
    }

    // ADDITIONAL METHOD - FLIGHT DEPARTURE
    public void depart(Reservation context) {
        // Flight takes off
        context.setStatus(ReservationStatus.FLOWN);
        // Could create FlownState if needed for post-flight actions
    }

    private boolean authorizedForOffload() {
        // Complex authorization logic
        // Check user role, reason codes, flight timing, etc.
        return false; // Simplified
    }
}