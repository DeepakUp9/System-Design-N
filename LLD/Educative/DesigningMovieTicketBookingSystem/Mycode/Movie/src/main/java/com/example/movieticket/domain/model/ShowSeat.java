package com.example.movieticket.domain.model;

import com.example.movieticket.domain.enums.SeatStatus;
import com.example.movieticket.domain.enums.SeatType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "show_seat", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"show_id", "seat_code"})
})
public class ShowSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "show_id", nullable = false)
    private Long showId;

    @Column(name = "seat_code", nullable = false, length = 16)
    private String seatCode; // e.g., "A5"

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Column(name = "held_by_user_id")
    private Long heldByUserId;

    @Column(name = "hold_expiry_at")
    private Instant holdExpiryAt;

    @Version
    private Long version; // optimistic locking

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    // Constructors
    public ShowSeat() {}

    public ShowSeat(Long showId, String seatCode, SeatType seatType, BigDecimal price) {
        this.showId = showId;
        this.seatCode = seatCode;
        this.seatType = seatType;
        this.price = price;
        this.status = SeatStatus.AVAILABLE;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }
    public String getSeatCode() { return seatCode; }
    public void setSeatCode(String seatCode) { this.seatCode = seatCode; }
    public SeatType getSeatType() { return seatType; }
    public void setSeatType(SeatType seatType) { this.seatType = seatType; }
    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }
    public Long getHeldByUserId() { return heldByUserId; }
    public void setHeldByUserId(Long heldByUserId) { this.heldByUserId = heldByUserId; }
    public Instant getHoldExpiryAt() { return holdExpiryAt; }
    public void setHoldExpiryAt(Instant holdExpiryAt) { this.holdExpiryAt = holdExpiryAt; }
    public Long getVersion() { return version; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
