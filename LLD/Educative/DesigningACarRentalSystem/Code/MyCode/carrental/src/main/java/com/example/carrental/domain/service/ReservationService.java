
package com.example.carrental.domain.service;

import com.example.carrental.domain.model.VehicleReservation;
import com.example.carrental.service.dto.ReserveVehicleRequest;
import com.example.carrental.service.dto.ReserveVehicleResponse;

import java.util.UUID;

public interface ReservationService {
    ReserveVehicleResponse reserveVehicle(ReserveVehicleRequest request);
    // other methods: cancelReservation, updateReservation, getReservation, listReservationsByCustomer...
    VehicleReservation getReservation(UUID id);

}

