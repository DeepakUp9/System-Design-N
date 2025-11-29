package com.example.carrental.service.notification.impl;

import com.example.carrental.domain.model.VehicleReservation;
import com.example.carrental.service.notification.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleNotificationPublisher implements NotificationPublisher {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publishReservationConfirmed(VehicleReservation r) {
        //publisher.publishEvent(new ReservationConfirmedEvent(this, r));
    }

    @Override
    public void publishReservationCancelled(VehicleReservation r, String reason) {

    }

    @Override
    public void publishReservationModified(VehicleReservation r) {

    }

    @Override
    public void publishOverdueNotification(VehicleReservation r, String fineId) {

    }
    // ... other events
}