package com.airline.reservation.state.impl;

import com.airline.core.exception.BusinessException;
import com.airline.reservation.domain.Reservation;
import com.airline.reservation.domain.ReservationStatus;
import com.airline.reservation.state.ReservationState;
import org.springframework.http.HttpStatus;

public class CheckedInState implements ReservationState {

    @Override
    public void confirm(Reservation context) {
        throw new BusinessException("Already checked in. No further confirmation needed.",
                "ERR_ALREADY_CHECKED_IN", HttpStatus.CONFLICT);
    }

    @Override
    public void cancel(Reservation context) {
        // Real logic: Check cancellation rules AFTER check-in
        // Usually: No cancellation, but possible with fee
        // Might require supervisor approval
        context.setStatus(ReservationStatus.CANCELLED);
        context.setCurrentState(new CancelledState());
    }

    @Override
    public void checkIn(Reservation context) {
        throw new BusinessException("Already checked in.",
                "ERR_ALREADY_CHECKED_IN", HttpStatus.CONFLICT);
    }

    @Override
    public void expire(Reservation context) {
        // Checked-in but didn't board? Could expire at gate closing time
        context.setStatus(ReservationStatus.EXPIRED);
        context.setCurrentState(new ExpiredState());
    }

    // ADDITIONAL METHOD SPECIFIC TO CHECKED-IN STATE
    public void board(Reservation context) {
        // Transition to boarding
        context.setStatus(ReservationStatus.BOARDED);
        context.setCurrentState(new BoardedState());
    }
}