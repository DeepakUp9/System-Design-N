package com.example.carrental.domain.repository;

import com.example.carrental.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository  extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByReservationId(UUID reservationId);
}
