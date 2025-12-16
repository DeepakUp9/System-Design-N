package com.airline.reservation.state;

import com.airline.reservation.domain.ReservationStatus;
import com.airline.reservation.state.impl.*;
import java.util.Map;

public class ReservationStateFactory {
    private static final Map<ReservationStatus, ReservationState> STATES = Map.of(
            ReservationStatus.PENDING, new PendingState(),
            ReservationStatus.CONFIRMED, new ConfirmedState(),
            ReservationStatus.CHECKED_IN, new CheckedInState(),
            ReservationStatus.CANCELLED, new CancelledState(),
            ReservationStatus.BOARDED, new BoardedState(),
            ReservationStatus.EXPIRED, new ExpiredState()
    );

    public static ReservationState getState(ReservationStatus status) {
        return STATES.getOrDefault(status, new PendingState());
    }
}