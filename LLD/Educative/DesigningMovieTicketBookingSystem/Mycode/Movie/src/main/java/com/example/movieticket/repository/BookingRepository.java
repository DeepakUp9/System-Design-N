package com.example.movieticket.repository;


import com.example.movieticket.domain.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByReservationToken(String token);
}
