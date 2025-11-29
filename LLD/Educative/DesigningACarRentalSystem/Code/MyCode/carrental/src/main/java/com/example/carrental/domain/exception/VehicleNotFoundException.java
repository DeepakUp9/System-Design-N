package com.example.carrental.domain.exception;

public class VehicleNotFoundException extends DomainException {
    public VehicleNotFoundException(String id) { super("Vehicle not found: " + id); }
}