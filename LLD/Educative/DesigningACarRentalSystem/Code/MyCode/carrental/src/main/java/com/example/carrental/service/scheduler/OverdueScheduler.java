package com.example.carrental.service.scheduler;

import com.example.carrental.domain.model.Fine;
import com.example.carrental.domain.model.ReservationStatus;
import com.example.carrental.domain.model.VehicleReservation;
import com.example.carrental.domain.repository.FineRepository;
import com.example.carrental.domain.repository.VehicleRepository;
import com.example.carrental.domain.repository.VehicleReservationRepository;
import com.example.carrental.service.notification.NotificationPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OverdueScheduler {

    private final VehicleReservationRepository reservationRepository;
    private final FineRepository fineRepository;
    private final NotificationPublisher notificationPublisher;
    private final VehicleRepository vehicleRepository;

    @Scheduled(fixedDelayString = "${rental.overdue.check-ms:60000}")
    @Transactional
    public void checkOverdueReservations() {
        Instant now = Instant.now();
        List<VehicleReservation> overdue = reservationRepository.findByEndTimeBeforeAndStatusIn(now, List.of(ReservationStatus.CONFIRMED, ReservationStatus.PICKED_UP));

        for (var r : overdue) {
            // compute fine: simple per-hour late fee or flat
            long secsLate = java.time.Duration.between(r.getEndTime(), now).getSeconds();
            long hoursLate = Math.max(1, secsLate / 3600);
            BigDecimal fineAmount = BigDecimal.valueOf(hoursLate).multiply(new BigDecimal("50.00")); // Rs 50/hr example

            Fine fine = new Fine();
            fine.setId(UUID.randomUUID());
            fine.setReservation(r);
            fine.setAmount(fineAmount);
            fine.setReason("Auto fine for overdue return");

            fineRepository.save(fine);

            // Notify
            notificationPublisher.publishOverdueNotification(r, fine.getId().toString());

            // optionally mark reservation as overdue status or keep status and attach fines
        }
    }
}

