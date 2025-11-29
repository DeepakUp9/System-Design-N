package com.example.carrental.domain.service.impl;

import com.example.carrental.domain.exception.*;
import com.example.carrental.domain.model.*;
import com.example.carrental.domain.repository.*;
import com.example.carrental.domain.service.PaymentService;
import com.example.carrental.domain.service.ReservationManagementService;
import com.example.carrental.service.dto.*;
import com.example.carrental.service.notification.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationManagementServiceImpl implements ReservationManagementService {

    private final VehicleReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final VehicleRepository vehicleRepository;
    private final EquipmentRepository equipmentRepository;
    private final ServiceRepository serviceRepository;
    private final PaymentService paymentService;
    private final NotificationPublisher notificationPublisher;

    // Cancellation rules: >48h full refund, 4-48h 50%, <4h no refund
    private static final Duration FULL_REFUND_WINDOW = Duration.ofHours(48);
    private static final Duration HALF_REFUND_WINDOW = Duration.ofHours(4);

    @Override
    @Transactional
    public CancelReservationResponse cancelReservation(CancelReservationRequest request) {
        var reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ReservationNotFoundException(request.reservationId().toString()));

        // only the reserver or receptionist allowed (simple check)
        if (!reservation.getAccount().getId().equals(request.customerId())) {
            throw new DomainException("Only the customer who created reservation can cancel it");
        }

        if (reservation.getStatus() == ReservationStatus.PICKED_UP || reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new CancellationNotAllowedException("Cannot cancel after pickup");
        }

        // compute refund amount based on timing
        Instant now = Instant.now();
        Duration untilStart = Duration.between(now, reservation.getStartTime());

        BigDecimal refundAmount = BigDecimal.ZERO;
        BigDecimal paidAmount = paymentRepository.findByReservationId(reservation.getId())
                .map(Payment::getAmount).orElse(BigDecimal.ZERO);

        if (untilStart.compareTo(FULL_REFUND_WINDOW) > 0) {
            // full refund
            refundAmount = paidAmount;
        } else if (untilStart.compareTo(HALF_REFUND_WINDOW) > 0) {
            // 50% refund of paid amount
            refundAmount = paidAmount.multiply(new BigDecimal("0.5"));
        } else {
            refundAmount = BigDecimal.ZERO;
        }

        // mark reservation cancelled
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // update vehicle status if no other reservation holds it
        var vehicle = reservation.getVehicle();
        boolean otherActive = reservationRepository.existsByVehicleIdAndActiveStatuses(vehicle.getId());
        if (!otherActive) {
            vehicle.setStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(vehicle);
        }

        // process refund if needed
        if (refundAmount.compareTo(BigDecimal.ZERO) > 0) {
            var paymentOpt = paymentRepository.findByReservationId(reservation.getId());
            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();
                String refundTx = paymentService.refund(payment.getId(), refundAmount, "CANCEL-" + reservation.getId());
                // you could store refundTx somewhere
            }
        }

        // notify
        notificationPublisher.publishReservationCancelled(reservation, request.reason());

        return new CancelReservationResponse(
                reservation.getId(),
                true,                                   // success
                "Reservation cancelled successfully",   // message
                refundAmount,
                "USD"                                   // currency - adjust as needed
        );
    }

    @Override
    @Transactional
    public ModifyReservationResponse modifyReservation(ModifyReservationRequest request) {
        var reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ReservationNotFoundException(request.reservationId().toString()));

        if (!reservation.getAccount().getId().equals(request.customerId())) {
            throw new DomainException("Only the customer who created reservation can modify it");
        }

        if (reservation.getStatus() == ReservationStatus.PICKED_UP || reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new DomainException("Cannot modify reservation after pickup");
        }

        // Check time window validity
        if (request.newStartTime().isAfter(request.newEndTime()) || request.newStartTime().equals(request.newEndTime())) {
            throw new ReservationConflictException("Invalid time window");
        }

        // If vehicle unchanged, check overlapping reservations except this one
        UUID vehicleId = reservation.getVehicle().getId();
        boolean hasOverlap = reservationRepository.existsOverlappingExcludingReservation(vehicleId, request.newStartTime(), request.newEndTime(), reservation.getId());
        if (hasOverlap) {
            throw new VehicleNotAvailableException(vehicleId.toString());
        }

        // update reservation fields
        reservation.setStartTime(request.newStartTime());
        reservation.setEndTime(request.newEndTime());

        // update equipments/services
        if (request.newEquipmentIds() != null) {
            reservation.setEquipments(new HashSet<>(equipmentRepository.findAllById(request.newEquipmentIds())));
        }
        if (request.newServiceIds() != null) {
            reservation.setServices(new HashSet<>(serviceRepository.findAllById(request.newServiceIds())));
        }

        // recalc price
        double newEstimated = /* reuse calculateEstimatedCost - implement/helper */ 0.0;
        reservation.setEstimatedCost(newEstimated);

        reservationRepository.save(reservation);

        // possibly capture extra payment if newEstimated > already paid (not implemented here)
        // send notification
        notificationPublisher.publishReservationModified(reservation);

        return new ModifyReservationResponse(
                reservation.getId(),
                true,
                "Reservation modified successfully",
                reservation.getStartTime(),
                reservation.getEndTime(),
                request.newEquipmentIds(),  // from the request
                request.newServiceIds(),    // from the request
                BigDecimal.ZERO,            // priceDifference (to be implemented)
                "USD"
        );
    }
}
