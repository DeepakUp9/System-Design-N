package com.example.movieticket.api.dto;

import com.example.movieticket.domain.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class BookingDetailsResponse {
    private Long bookingId;
    private Long userId;
    private Long showId;
    private List<String> seatCodes;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private Instant createdAt;

    // getters & setters
}
