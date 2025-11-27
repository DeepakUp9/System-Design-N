package com.example.movieticket.api.dto;

import java.time.Instant;

public class ErrorResponse {
    private String message;
    private String errorCode;
    private Instant timestamp = Instant.now();

    public ErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    // getters
}
