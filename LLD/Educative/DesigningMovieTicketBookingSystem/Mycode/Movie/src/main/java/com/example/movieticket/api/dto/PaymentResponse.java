package com.example.movieticket.api.dto;

import com.example.movieticket.domain.enums.PaymentStatus;

public class PaymentResponse {
    private Long paymentAttemptId;
    private PaymentStatus status;
    private String gatewayReference;

    // getters & setters
}

