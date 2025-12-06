package com.hms.hotel.repository;

import com.hms.hotel.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data Repository for the Payment Entity.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Standard CRUD operations for Payment objects created via the PaymentFactory
}