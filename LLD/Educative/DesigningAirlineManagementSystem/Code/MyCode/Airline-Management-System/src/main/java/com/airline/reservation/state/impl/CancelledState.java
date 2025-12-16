package com.airline.reservation.state.impl;

import com.airline.core.exception.BusinessException;
import com.airline.reservation.domain.Reservation;
import com.airline.reservation.state.ReservationState;
import org.springframework.http.HttpStatus;

public class CancelledState implements ReservationState {

    @Override
    public void confirm(Reservation context) {
        throw new BusinessException("Cannot confirm a cancelled reservation. Please book a new flight.",
                "ERR_CANCELLED_RESERVATION", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void cancel(Reservation context) {
        throw new BusinessException("Reservation is already cancelled.",
                "ERR_ALREADY_CANCELLED", HttpStatus.CONFLICT);
    }

    @Override
    public void checkIn(Reservation context) {
        throw new BusinessException("Cannot check-in a cancelled reservation.",
                "ERR_CANCELLED_RESERVATION", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void expire(Reservation context) {
        // Already cancelled, expiration doesn't apply
        // Could archive the record after certain time
        // No state change needed
    }
}