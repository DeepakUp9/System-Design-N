package com.hms.hotel.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // PRIVATE CONSTRUCTOR - GOOD!
    private Booking() {}
    /**
     * Allows controlled, public update of the Booking status by a Service.
     */
    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    /**
     * Factory method to start the Builder process.
     */
    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    // ========== INNER BUILDER CLASS ==========
    public static class BookingBuilder {
        private Room room;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private int numberOfGuests;
        private BigDecimal totalPrice;

        public BookingBuilder withRoom(Room room) {
            this.room = room;
            return this;
        }

        public BookingBuilder withCheckInDate(LocalDate checkInDate) {
            this.checkInDate = checkInDate;
            return this;
        }

        public BookingBuilder withCheckOutDate(LocalDate checkOutDate) {
            this.checkOutDate = checkOutDate;
            return this;
        }

        public BookingBuilder withNumberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public BookingBuilder withTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Booking build() {
            // Validation
            if (room == null || checkInDate == null || checkOutDate == null || totalPrice == null) {
                throw new IllegalStateException("Missing required fields");
            }
            if (numberOfGuests <= 0) {
                throw new IllegalStateException("Number of guests must be positive");
            }
            if (!checkOutDate.isAfter(checkInDate)) {
                throw new IllegalArgumentException("Check-out must be after check-in");
            }

            // Create Booking (can access private constructor)
            Booking booking = new Booking();
            booking.room = this.room;
            booking.checkInDate = this.checkInDate;
            booking.checkOutDate = this.checkOutDate;
            booking.numberOfGuests = this.numberOfGuests;
            booking.totalPrice = this.totalPrice;
            booking.status = Status.PENDING;

            return booking;
        }
    }

    // ========== GETTERS (for JPA) ==========
    public Long getId() { return id; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public int getNumberOfGuests() { return numberOfGuests; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public Status getStatus() { return status; }

    // ========== NO SETTERS NEEDED ==========
    // Builder sets fields directly
}