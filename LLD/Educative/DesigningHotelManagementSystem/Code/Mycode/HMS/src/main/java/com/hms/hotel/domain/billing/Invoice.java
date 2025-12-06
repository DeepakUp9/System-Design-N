package com.hms.hotel.domain.billing;

import com.hms.hotel.entity.Booking;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * LLD: Builder Pattern - The Product
 * Represents the final, complex Invoice document.
 */
public class Invoice {

    private final String invoiceId;
    private final LocalDateTime generationDate;
    private final Booking bookingReference;
    private final List<LineItem> lineItems; // Detailed breakdown
    private final BigDecimal subTotal;
    private final BigDecimal totalTaxes;
    private final BigDecimal grandTotal;
    private final String paymentStatus; // e.g., PAID, PARTIAL, DUE

    // Private constructor ensures only the Builder can create the instance
    private Invoice(InvoiceBuilder builder) {
        this.invoiceId = builder.invoiceId;
        this.generationDate = builder.generationDate;
        this.bookingReference = builder.bookingReference;
        this.lineItems = Collections.unmodifiableList(builder.lineItems);
        this.subTotal = builder.subTotal;
        this.totalTaxes = builder.totalTaxes;
        this.grandTotal = builder.grandTotal;
        this.paymentStatus = builder.paymentStatus;
    }

    // Static method to obtain the Builder (Factory method)
    public static InvoiceBuilder builder(Booking booking) {
        return new InvoiceBuilder(booking);
    }

    // Getters (omitted for brevity)
    public String getInvoiceId() { return invoiceId; }
    public BigDecimal getGrandTotal() { return grandTotal; }
    // ...

    // Helper class for line items (could be a separate record/class)
    public record LineItem(String description, BigDecimal amount) {}


    // -----------------------------------------------------
    // LLD: Builder Pattern - Static Nested Builder Class
    // -----------------------------------------------------
    public static class InvoiceBuilder {

        // --- Required Parameters (Passed in constructor) ---
        private final Booking bookingReference;

        // --- Optional/Calculated Parameters ---
        private String invoiceId;
        private LocalDateTime generationDate;
        private List<LineItem> lineItems;
        private BigDecimal subTotal = BigDecimal.ZERO;
        private BigDecimal totalTaxes = BigDecimal.ZERO;
        private BigDecimal grandTotal = BigDecimal.ZERO;
        private String paymentStatus = "DUE"; // Default

        public InvoiceBuilder(Booking booking) {
            if (booking == null) {
                throw new IllegalArgumentException("Invoice must reference a valid Booking.");
            }
            this.bookingReference = booking;
            this.invoiceId = "INV-" + booking.getId() + "-" + System.currentTimeMillis();
            this.generationDate = LocalDateTime.now();
        }

        public InvoiceBuilder withLineItems(List<LineItem> items) {
            this.lineItems = items;
            return this;
        }

        public InvoiceBuilder withSubTotal(BigDecimal subTotal) {
            this.subTotal = subTotal;
            return this;
        }

        public InvoiceBuilder withTotalTaxes(BigDecimal totalTaxes) {
            this.totalTaxes = totalTaxes;
            return this;
        }

        public InvoiceBuilder withPaymentStatus(String status) {
            this.paymentStatus = status;
            return this;
        }

        /**
         * The final build method, containing the complex construction logic.
         */
        public Invoice build() {
            // Edge Case: Final financial validation
            // 1. Calculate Grand Total if not explicitly set (to ensure consistency)
            if (this.grandTotal.compareTo(BigDecimal.ZERO) == 0) {
                this.grandTotal = this.subTotal.add(this.totalTaxes);
            }

            // 2. Cross-check against the booking's total price
            if (this.grandTotal.compareTo(bookingReference.getTotalPrice()) != 0) {
                // In production, this would be a severe error, but here we enforce the calculated value.
                System.err.println("Warning: Invoice Grand Total differs from Booking Total Price.");
            }

            return new Invoice(this);
        }
    }
}