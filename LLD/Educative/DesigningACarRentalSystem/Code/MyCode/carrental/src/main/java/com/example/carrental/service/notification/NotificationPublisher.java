package com.example.carrental.service.notification;

import com.example.carrental.domain.model.VehicleReservation;

public interface NotificationPublisher {
    void publishReservationConfirmed(VehicleReservation r);
    void publishReservationCancelled(VehicleReservation r, String reason);
    void publishReservationModified(VehicleReservation r);
    void publishOverdueNotification(VehicleReservation r, String fineId);
}
