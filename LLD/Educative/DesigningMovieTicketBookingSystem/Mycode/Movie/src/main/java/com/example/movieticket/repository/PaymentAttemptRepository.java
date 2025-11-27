package com.example.movieticket.repository;

import com.example.movieticket.domain.model.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, Long> {
    Optional<PaymentAttempt> findByIdempotencyKey(String idempotencyKey);
}
