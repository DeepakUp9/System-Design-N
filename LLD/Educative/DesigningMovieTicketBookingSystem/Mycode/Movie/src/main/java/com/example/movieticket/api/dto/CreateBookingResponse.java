package com.example.movieticket.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreateBookingResponse {
    private Long bookingId;
    private String reservationToken;
    private List<String> lockedSeats;
    private BigDecimal totalAmount;

    // getters & setters
}
