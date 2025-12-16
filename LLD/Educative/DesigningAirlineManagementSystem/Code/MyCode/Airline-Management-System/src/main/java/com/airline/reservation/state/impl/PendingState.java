package com.airline.reservation.state.impl;

import com.airline.core.exception.BusinessException;
import com.airline.reservation.domain.Reservation;
import com.airline.reservation.domain.ReservationStatus;
import com.airline.reservation.state.ReservationState;
import org.springframework.http.HttpStatus;

public class PendingState implements ReservationState {

    @Override
    public void confirm(Reservation context) {
        // Business Logic: Transition to Confirmed
        context.setStatus(ReservationStatus.CONFIRMED);
        context.setCurrentState(new ConfirmedState());
        // In Step 4, we will add Kafka events here
    }

    @Override
    public void cancel(Reservation context) {
        context.setStatus(ReservationStatus.CANCELLED);
        context.setCurrentState(new CancelledState());
    }

    @Override
    public void checkIn(Reservation context) {
        throw new BusinessException("Cannot check-in a pending reservation. Pay first.",
                "ERR_INVALID_STATE", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void expire(Reservation context) {
        context.setStatus(ReservationStatus.EXPIRED);
    }
}