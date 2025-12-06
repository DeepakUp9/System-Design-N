package com.hms.hotel.repository;

import com.hms.hotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data Repository for the Booking Entity.
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Add custom finder methods here as needed, e.g., findByGuestId(Long guestId)
}