package com.airline.reservation.domain;

import com.airline.core.domain.BaseEntity;
import com.airline.flight.domain.Flight;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_segments")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    private Integer sequenceOrder;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;
}