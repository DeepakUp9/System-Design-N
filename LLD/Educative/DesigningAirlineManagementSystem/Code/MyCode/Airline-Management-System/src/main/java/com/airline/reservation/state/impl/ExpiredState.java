package com.airline.reservation.state.impl;

import com.airline.core.exception.BusinessException;
import com.airline.reservation.domain.Reservation;
import com.airline.reservation.domain.ReservationStatus;
import com.airline.reservation.state.ReservationState;
import org.springframework.http.HttpStatus;

public class ExpiredState implements ReservationState {

    @Override
    public void confirm(Reservation context) {
        // Some airlines allow reinstatement with fee
        if (withinReinstatementWindow()) {
            context.setStatus(ReservationStatus.CONFIRMED);
            context.setCurrentState(new ConfirmedState());
            applyReinstatementFee(context);
        } else {
            throw new BusinessException("Reservation expired. Cannot reinstate. Please book a new flight.",
                    "ERR_EXPIRED_RESERVATION", HttpStatus.GONE);
        }
    }

    @Override
    public void cancel(Reservation context) {
        // Already expired, no cancellation needed
        // But might trigger cleanup/archiving
        archiveReservation(context);
    }

    @Override
    public void checkIn(Reservation context) {
        throw new BusinessException("Cannot check-in an expired reservation.",
                "ERR_EXPIRED_RESERVATION", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void expire(Reservation context) {
        // Already expired
        // Could update expiration timestamp
    }

    private boolean withinReinstatementWindow() {
        // Check if expired within last X hours
        // Business rule: e.g., 2 hours for missed check-in
        return true; // Simplified
    }

    private void applyReinstatementFee(Reservation context) {
        // Add fee to reservation
        // Common: $50-100 reinstatement fee
    }

    private void archiveReservation(Reservation context) {
        // Move to historical/archive table
        // Free up resources
    }
}