package com.example.carrental.domain.exception;

public class CancellationNotAllowedException extends DomainException {
    public CancellationNotAllowedException(String msg) { super(msg); }
}
