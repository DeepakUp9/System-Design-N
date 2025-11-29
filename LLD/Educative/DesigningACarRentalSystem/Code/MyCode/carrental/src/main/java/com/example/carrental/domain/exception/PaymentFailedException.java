package com.example.carrental.domain.exception;

public class PaymentFailedException extends DomainException {
    public PaymentFailedException(String msg) { super("Payment failed: " + msg); }
}
