package com.airline.reservation.state.impl;

import com.airline.core.exception.BusinessException;
import com.airline.reservation.domain.Reservation;
import com.airline.reservation.domain.ReservationStatus;
import com.airline.reservation.state.ReservationState;
import org.springframework.http.HttpStatus;

public class ConfirmedState implements ReservationState {

    @Override
    public void confirm(Reservation context) {
        throw new BusinessException("Reservation is already confirmed.",
                "ERR_ALREADY_CONFIRMED", HttpStatus.CONFLICT);
    }

    @Override
    public void cancel(Reservation context) {
        // Production logic: Check if within cancellation window (e.g., 24hrs before)
        context.setStatus(ReservationStatus.CANCELLED);
        context.setCurrentState(new CancelledState());
    }

    @Override
    public void checkIn(Reservation context) {
        context.setStatus(ReservationStatus.CHECKED_IN);
        context.setCurrentState(new CheckedInState());
    }

    @Override
    public void expire(Reservation context) {
        // Confirmed tickets don't just "expire" usually, they are either used or cancelled
        throw new BusinessException("Confirmed reservations cannot expire.",
                "ERR_INVALID_TRANSITION", HttpStatus.BAD_REQUEST);
    }
}