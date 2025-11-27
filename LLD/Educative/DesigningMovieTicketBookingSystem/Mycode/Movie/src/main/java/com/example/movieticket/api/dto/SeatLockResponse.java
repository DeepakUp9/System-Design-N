package com.example.movieticket.api.dto;

import java.util.List;

public class SeatLockResponse {
    private List<String> lockedSeats;
    private List<String> unavailableSeats;
    private String reservationToken;

    // getters & setters
}
