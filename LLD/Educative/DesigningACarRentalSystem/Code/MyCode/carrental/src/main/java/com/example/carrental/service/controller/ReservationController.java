package com.example.carrental.service.controller;

import com.example.carrental.domain.service.ReservationManagementService;
import com.example.carrental.domain.service.ReservationService;
import com.example.carrental.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationManagementService managementService;

    @PostMapping
    public ResponseEntity<ReserveVehicleResponse> reserve(@RequestBody ReserveVehicleRequest req) {
        var resp = reservationService.reserveVehicle(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModifyReservationResponse> modify(@PathVariable UUID id, @RequestBody ModifyReservationRequest req) {
        var resp = managementService.modifyReservation(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<CancelReservationResponse> cancel(@PathVariable UUID id, @RequestBody CancelReservationRequest req) {
        var resp = managementService.cancelReservation(req);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleReservationDto> get(@PathVariable UUID id) {
        var r = reservationService.getReservation(id); // implement this method
        return ResponseEntity.ok(VehicleReservationDto.from(r));
    }
}

