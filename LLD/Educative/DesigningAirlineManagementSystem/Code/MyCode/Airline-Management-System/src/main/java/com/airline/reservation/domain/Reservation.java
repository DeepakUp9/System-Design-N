package com.airline.reservation.domain;

import com.airline.core.domain.BaseEntity;
import com.airline.reservation.state.ReservationState;
import com.airline.reservation.state.ReservationStateFactory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String pnr; // Passenger Name Record

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private BigDecimal totalAmount;

    // This is the bridge to our LLD State Pattern
    // We don't persist the State object, only the Status enum
    @Transient
    private ReservationState currentState;

    /**
     * This method is the "Context" entry point.
     * It delegates the business logic to the current state implementation.
     */
    public void confirm() {
        currentState.confirm(this);
    }

    public void cancel() {
        currentState.cancel(this);
    }

    @PostLoad
    protected void fillState() {
        this.currentState = ReservationStateFactory.getState(this.getStatus());
    }
}