package com.airline.reservation.state;

import com.airline.reservation.domain.Reservation;

/**
 * The State Interface.
 * Every concrete state will implement these methods.
 */
public interface ReservationState {
    void confirm(Reservation context);
    void cancel(Reservation context);
    void checkIn(Reservation context);
    void expire(Reservation context);
}