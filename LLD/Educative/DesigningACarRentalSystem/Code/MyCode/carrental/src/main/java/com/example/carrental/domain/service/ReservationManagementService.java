package com.example.carrental.domain.service;

import com.example.carrental.service.dto.CancelReservationRequest;
import com.example.carrental.service.dto.CancelReservationResponse;
import com.example.carrental.service.dto.ModifyReservationRequest;
import com.example.carrental.service.dto.ModifyReservationResponse;

public interface ReservationManagementService {
    CancelReservationResponse cancelReservation(CancelReservationRequest request);
    ModifyReservationResponse modifyReservation(ModifyReservationRequest request);
}
