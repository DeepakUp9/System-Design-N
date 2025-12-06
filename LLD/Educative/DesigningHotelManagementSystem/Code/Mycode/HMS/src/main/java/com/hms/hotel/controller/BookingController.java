package com.hms.hotel.controller;

import com.hms.hotel.dto.BookingRequest;
import com.hms.hotel.entity.Booking;
import com.hms.hotel.service.BookingProcessorService; // New dedicated service for booking flow
import com.hms.hotel.service.RateCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final RateCalculationService rateService;
    private final BookingProcessorService bookingProcessorService;

    // We will inject a new BookingProcessorService to encapsulate the LLD orchestration.
    public BookingController(RateCalculationService rateService, BookingProcessorService bookingProcessorService) {
        this.rateService = rateService;
        this.bookingProcessorService = bookingProcessorService;
    }

    // Endpoint 1: Pre-booking price calculation (Strategy Pattern)
    @PostMapping("/price")
    public ResponseEntity<BigDecimal> calculatePrice(@RequestBody BookingRequest request) {
        BigDecimal totalPrice = bookingProcessorService.calculatePriceForRequest(request);
        return ResponseEntity.ok(totalPrice);
    }

    // Endpoint 2: Core Booking Creation (Builder, Factory, Observer)
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        // The service method orchestrates:
        // 1. Price calculation (Strategy)
        // 2. Booking creation (Builder)
        // 3. Payment creation (Factory)
        // 4. Room state check (State)
        Booking booking = bookingProcessorService.processNewBooking(request);
        return ResponseEntity.status(201).body(booking);
    }

    // Endpoint 3: Confirm Booking (Observer Pattern Publisher)
    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long bookingId) {
        // This triggers the AuditTrailListener (Transactional) and EmailNotificationListener (Async)
        Booking confirmedBooking = bookingProcessorService.confirmBooking(bookingId);
        return ResponseEntity.ok(confirmedBooking);
    }
}