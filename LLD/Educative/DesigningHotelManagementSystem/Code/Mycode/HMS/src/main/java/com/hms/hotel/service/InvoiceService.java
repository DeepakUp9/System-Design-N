package com.hms.hotel.service;

import com.hms.hotel.domain.billing.Invoice;
import com.hms.hotel.domain.billing.Invoice.LineItem;
import com.hms.hotel.entity.Booking;
import com.hms.hotel.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * LLD: Builder Pattern - Director (Spring Service)
 * Contains the logic for constructing the Invoice product step-by-step.
 */
@Service
public class InvoiceService {

    private final BookingRepository bookingRepository;
    private final TaxCalculationService taxService;
    // Assume a PaymentRepository is available to find paid amounts

    public InvoiceService(BookingRepository bookingRepository, TaxCalculationService taxService) {
        this.bookingRepository = bookingRepository;
        this.taxService = taxService;
    }

    /**
     * Builds a detailed Invoice for a completed or confirmed booking.
     */
    public Invoice generateDetailedInvoice(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found for invoice generation: " + bookingId));

        // 1. Calculate and itemize components
        BigDecimal totalDays = BigDecimal.valueOf(booking.getCheckInDate().until(booking.getCheckOutDate()).getDays());
        BigDecimal totalBookingPrice = booking.getTotalPrice(); // This is the final grand total from Step 12

        // Simulate breakdown (In a real system, these would be saved in a breakdown table)

        // --- Step 2: Assemble Line Items ---
        List<LineItem> items = new ArrayList<>();

        // Base Line Item (Assuming 85% is net revenue, 15% is tax for simplicity)
        BigDecimal netRevenue = totalBookingPrice.divide(new BigDecimal("1.15"), 2, RoundingMode.HALF_UP);
        BigDecimal totalTaxAmount = totalBookingPrice.subtract(netRevenue);

        items.add(new LineItem("Room Charges (" + booking.getRoom().getRoomNumber() + ") for " + totalDays + " nights", netRevenue));
        // items.add(new LineItem("Additional Service Fee (e.g., Minibar)", new BigDecimal("25.00"))); // Example Add-on

        // --- Step 3: Build using the Builder ---
        Invoice invoice = Invoice.builder(booking)
                .withLineItems(items)
                .withSubTotal(netRevenue) // Subtotal before taxes
                .withTotalTaxes(totalTaxAmount)
                // Grand Total calculation is handled internally by the Builder's final check
                .withPaymentStatus("PAID") // Assume payment processed successfully
                .build(); // Final validation and construction

        return invoice;
    }
}