package com.example.carrental.domain.exception;

public class ReservationNotFoundException extends DomainException {
    public ReservationNotFoundException(String id) { super("Reservation not found: " + id); }
}