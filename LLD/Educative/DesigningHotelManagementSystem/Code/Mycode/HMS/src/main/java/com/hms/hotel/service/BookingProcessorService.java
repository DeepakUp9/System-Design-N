package com.hms.hotel.service;

import com.hms.hotel.domain.rate.BookingDetails;
import com.hms.hotel.dto.BookingRequest;
import com.hms.hotel.entity.Booking;
import com.hms.hotel.entity.Room;
import com.hms.hotel.repository.BookingRepository;
import com.hms.hotel.repository.RoomRepository;
import com.hms.hotel.service.factory.PaymentFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Service
public class BookingProcessorService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final RateCalculationService rateService;
    private final PaymentFactory paymentFactory;
    private final BookingService bookingService; // For Observer Pattern publishing
    private final TaxCalculationService taxService; // Inject new service

    public BookingProcessorService(RoomRepository roomRepository, BookingRepository bookingRepository,
                                   RateCalculationService rateService, PaymentFactory paymentFactory,
                                   BookingService bookingService, TaxCalculationService taxService) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.rateService = rateService;
        this.paymentFactory = paymentFactory;
        this.bookingService = bookingService;
        this.taxService = taxService;
    }

    // Helper to map DTO to LLD object
    private BookingDetails mapToBookingDetails(BookingRequest request) {
        return new BookingDetails(
                null, // roomType is derived from Room entity in a real scenario
                (int) request.checkInDate().until(request.checkOutDate()).getDays(),
                request.isLoyaltyMember(),
                request.corporateCode(),
                request.checkInDate(),
                request.checkOutDate()
        );
    }

    // Exposed method for price check (Strategy Pattern)
    public BigDecimal calculatePriceForRequest(BookingRequest request) {
        return rateService.calculateTotalPrice(
                request.roomNumber(),
                mapToBookingDetails(request)
        );
    }

    /**
     * Orchestrates the entire booking process, utilizing multiple patterns.
     */
    @Transactional
    public Booking processNewBooking(BookingRequest request) {
        // 1. Check Room Availability (State Pattern: implicit check)
        Room room = roomRepository.findByRoomNumber(request.roomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.roomNumber()));

        if (!room.getStatusName().equals("VACANT")) {
            throw new IllegalStateException("Room is not available for booking. Current status: " + room.getStatusName());
        }

        // 2. Calculate Base/Discounted Price (Strategy Pattern - Rate)
        BigDecimal discountedRatePerNight = rateService.calculateTotalPrice(
                request.roomNumber(),
                mapToBookingDetails(request)
        ).divide(BigDecimal.valueOf(request.checkInDate().until(request.checkOutDate()).getDays()), 2, RoundingMode.HALF_UP);

        // NEW STEP: 2.5 Calculate Tax/Fees (Strategy Pattern - Tax)
        // Edge Case: Taxes are often based on hotel location, simulated here.
        List<String> requiredTaxes = Arrays.asList("VAT", "CITY_FEE");
        BigDecimal totalTaxPerNight = taxService.calculateTotalTaxPerNight(discountedRatePerNight, requiredTaxes);

        // Final Price Calculation
        BigDecimal finalRatePerNight = discountedRatePerNight.add(totalTaxPerNight);
        BigDecimal finalTotalPrice = finalRatePerNight.multiply(BigDecimal.valueOf(request.checkInDate().until(request.checkOutDate()).getDays()));

        // 3. Create Booking (Builder Pattern) - Use the FINAL price
        Booking newBooking = Booking.builder()
                .withRoom(room)
                .withCheckInDate(request.checkInDate())
                .withCheckOutDate(request.checkOutDate())
                .withNumberOfGuests(request.numberOfGuests())
                .withTotalPrice(finalTotalPrice) // IMPORTANT: Builder now uses the final price
                .build();

        // 4. Persist Booking
        newBooking = bookingRepository.save(newBooking);

        // 5. Create Payment Record (Factory Pattern)
        paymentFactory.createPayment(finalTotalPrice, request.paymentMethod(), newBooking);

        // 6. Optionally: update room status to PENDING/BLOCKED (can be done here or in the confirm step)

        return newBooking;
    }

    // Delegate for the Observer Pattern (from BookingController)
    @Transactional
    public Booking confirmBooking(Long bookingId) {
        return bookingService.confirmBooking(bookingId);
    }
}