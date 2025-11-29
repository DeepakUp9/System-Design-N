package com.example.carrental.domain.exception;

public class VehicleNotAvailableException extends DomainException {
    public VehicleNotAvailableException(String id) { super("Vehicle not available for requested time: " + id); }
}
