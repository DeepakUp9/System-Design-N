package com.example.carrental.domain.exception;

public class ReservationConflictException extends DomainException {
    public ReservationConflictException(String msg) { super(msg); }
}