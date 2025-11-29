package com.example.carrental.domain.service.impl;

import com.example.carrental.domain.model.Payment;
import com.example.carrental.domain.model.VehicleReservation;
import com.example.carrental.domain.repository.PaymentRepository;
import com.example.carrental.domain.repository.VehicleReservationRepository;
import com.example.carrental.domain.service.PaymentService;
import com.example.carrental.domain.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SimplePaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final VehicleReservationRepository reservationRepository;

    @Override
    @Transactional
    public String capture(UUID reservationId, BigDecimal amount, String currency, String method, String idempotencyKey) {
        // idempotency: if payment exists with same idempotency key, return tx id
        if (idempotencyKey != null) {
            Optional<Payment> existing = paymentRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return existing.get().getTransactionId();
            }
        }

        VehicleReservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId.toString()));

        Payment p = new Payment();
        p.setId(UUID.randomUUID());
        p.setReservation(r);
        p.setAmount(amount);
        p.setCurrency(currency);
        p.setMethod(method);
        p.setStatus("COMPLETED"); // simulation: always succeed
        p.setTransactionId("TXN-" + UUID.randomUUID());
        p.setCreatedAt(java.time.Instant.now());
        p.setIdempotencyKey(idempotencyKey);
        paymentRepository.save(p);

        return p.getTransactionId();
    }

    @Override
    @Transactional
    public String refund(UUID paymentId, BigDecimal amount, String idempotencyKey) {
        Payment p = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new DomainException("Payment not found: " + paymentId));
        // idempotency: check if a refund with same idempotency already exists - for simplicity omitted
        // simulate refund
        p.setStatus("REFUNDED");
        paymentRepository.save(p);
        return "RFND-" + UUID.randomUUID();
    }
}
