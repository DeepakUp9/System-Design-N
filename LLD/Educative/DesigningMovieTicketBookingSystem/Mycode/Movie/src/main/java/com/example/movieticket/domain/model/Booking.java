package com.example.movieticket.domain.model;

import com.example.movieticket.domain.enums.BookingStatus;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long showId;

    @ElementCollection
    @CollectionTable(name = "booking_seats", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "seat_code")
    private List<String> seatCodes;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private BigDecimal totalAmount;

    private Instant createdAt;

    @Column(name = "reservation_token", unique = true)
    private String reservationToken; // optional token-based reservation

    @Version
    private Long version;

    public Booking() {}

    public Booking(Long userId, Long showId, List<String> seatCodes, BookingStatus status, BigDecimal totalAmount, String reservationToken) {
        this.userId = userId;
        this.showId = showId;
        this.seatCodes = seatCodes;
        this.status = status;
        this.totalAmount = totalAmount;
        this.reservationToken = reservationToken;
        this.createdAt = Instant.now();
    }

    // Getters / setters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }
    public List<String> getSeatCodes() { return seatCodes; }
    public void setSeatCodes(List<String> seatCodes) { this.seatCodes = seatCodes; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Instant getCreatedAt() { return createdAt; }
    public String getReservationToken() { return reservationToken; }
    public void setReservationToken(String reservationToken) { this.reservationToken = reservationToken; }
}
