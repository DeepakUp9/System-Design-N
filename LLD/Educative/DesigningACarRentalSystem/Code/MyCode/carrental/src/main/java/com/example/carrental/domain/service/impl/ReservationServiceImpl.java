package com.example.carrental.domain.service.impl;


import com.example.carrental.domain.exception.DomainException;
import com.example.carrental.domain.exception.ReservationConflictException;
import com.example.carrental.domain.exception.VehicleNotAvailableException;
import com.example.carrental.domain.exception.VehicleNotFoundException;
import com.example.carrental.domain.model.*;
import com.example.carrental.domain.repository.*;
import com.example.carrental.domain.service.ReservationService;
import com.example.carrental.service.dto.ReserveVehicleRequest;
import com.example.carrental.service.dto.ReserveVehicleResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final VehicleRepository vehicleRepository;
    private final VehicleReservationRepository reservationRepository;
    private final AccountRepository accountRepository;
    private final EquipmentRepository equipmentRepository;
    private final ServiceRepository serviceRepository;
    private final EntityManager em;

    private static final int LOCK_RETRY = 3;
    private static final long LOCK_RETRY_DELAY_MS = 150L;

    @Override
    @Transactional
    public ReserveVehicleResponse reserveVehicle(ReserveVehicleRequest request) {
        // Validate times
        if (request.startTime().isAfter(request.endTime()) || request.startTime().equals(request.endTime())) {
            throw new ReservationConflictException("Invalid time window");
        }

        UUID vehicleId = request.vehicleId();
        UUID customerId = request.customerId();
        Instant start = request.startTime();
        Instant end = request.endTime();

        // Ensure account exists
        Account account = accountRepository.findById(customerId)
                .orElseThrow(() -> new DomainException("Customer account not found: " + customerId));
        // Try to get vehicle with PESSIMISTIC_WRITE to avoid concurrent allocations
        Vehicle vehicle = tryLockVehicle(vehicleId);

        // Re-check availability: overlapping reservations
        boolean existsOverlap = reservationRepository.existsOverlapping(vehicleId, start, end);
        if (existsOverlap) {
            throw new VehicleNotAvailableException(vehicleId.toString());
        }

        // Build reservation
        VehicleReservation reservation = new VehicleReservation();
        reservation.setId(UUID.randomUUID());
        reservation.setVehicle(vehicle);
        reservation.setAccount(account);
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(Instant.now());

        // Attach equipments and services if present
        if (request.equipmentIds() != null && !request.equipmentIds().isEmpty()) {
            List<Equipment> eqs = equipmentRepository.findAllById(request.equipmentIds());
            reservation.setEquipments(new HashSet<>(eqs));
        }
        if (request.serviceIds() != null && !request.serviceIds().isEmpty()) {
            List<com.example.carrental.domain.model.Service> svs = serviceRepository.findAllById(request.serviceIds());
            reservation.setServices(new HashSet<>(svs));
        }

        // calculate estimated cost (simple example)
        double estimated = calculateEstimatedCost(vehicle, reservation.getStartTime(), reservation.getEndTime(),
                reservation.getEquipments(), reservation.getServices());
        reservation.setEstimatedCost(estimated);

        // Persist reservation
        reservationRepository.save(reservation);

        // Update vehicle status to RESERVED
        vehicle.setStatus(VehicleStatus.RESERVED);
        vehicleRepository.save(vehicle);

        // If payNow flagged, delegate to PaymentService (not implemented here) or create Payment with PENDING
        // Payment creation/integration handled in PaymentService — we can call it here.

        return new ReserveVehicleResponse(reservation.getId(), vehicle.getId(), account.getId(),
                reservation.getStartTime(), reservation.getEndTime(), reservation.getStatus().name());
    }

    @Override
    public VehicleReservation getReservation(UUID id) {
        return null;
    }

    private Vehicle tryLockVehicle(UUID vehicleId) {
        int attempts = 0;
        while (true) {
            attempts++;
            try {
                // Use repository method annotated with @Lock(PESSIMISTIC_WRITE)
                return vehicleRepository.findByIdForUpdate(vehicleId)
                        .orElseThrow(() -> new VehicleNotFoundException(vehicleId.toString()));
            } catch (Exception e) {
                if (attempts >= LOCK_RETRY) {
                    throw new ReservationConflictException("Unable to lock vehicle for reservation (contention). Try again later.");
                }
                try {
                    Thread.sleep(LOCK_RETRY_DELAY_MS);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private double calculateEstimatedCost(Vehicle vehicle, Instant start, Instant end,
                                          Set<Equipment> equipments,
                                          Set<com.example.carrental.domain.model.Service> services) {
        long seconds = end.getEpochSecond() - start.getEpochSecond();
        long hours = Math.max(1, seconds / 3600);

        // Safe conversion with null checks
        double hourlyRate = 0;
        double dailyRate = 0;

        if (vehicle.getHourlyRate() != null) {
            hourlyRate = vehicle.getHourlyRate().doubleValue();
        }
        if (vehicle.getDailyRate() != null) {
            dailyRate = vehicle.getDailyRate().doubleValue();
        }

        // Use hourly rate if available and positive, otherwise use daily rate converted to hourly
        double hourlyCost = (hourlyRate > 0) ? hourlyRate : (dailyRate / 24.0);
        double base = hours * hourlyCost;

        double eqCost = (equipments == null ? 0.0 : equipments.stream()
                .mapToDouble(e -> e.getPrice() != null ? e.getPrice().doubleValue() : 0.0)
                .sum());
        double svcCost = (services == null ? 0.0 : services.stream()
                .mapToDouble(s -> s.getPrice() != null ? s.getPrice().doubleValue() : 0.0)
                .sum());

        return base + eqCost + svcCost;
    }
}
