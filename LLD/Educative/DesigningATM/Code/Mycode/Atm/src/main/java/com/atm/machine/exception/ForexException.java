package com.atm.machine.exception;

import org.springframework.http.HttpStatus;

public class ForexException extends AtmException {

    public ForexException(String sourceCurrency, String targetCurrency) {
        super("Currency conversion not supported: " + sourceCurrency + " to " + targetCurrency +
                        ". Supported currencies: USD, EUR, GBP, JPY",
                HttpStatus.BAD_REQUEST);
    }

    public ForexException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}