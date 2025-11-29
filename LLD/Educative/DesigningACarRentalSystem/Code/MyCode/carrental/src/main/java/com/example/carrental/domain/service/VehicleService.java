package com.example.carrental.domain.service;

import com.example.carrental.domain.model.Vehicle;
import com.example.carrental.domain.model.VehicleType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface VehicleService {
    List<Vehicle> searchAvailable(VehicleType type, UUID branchId, Instant start, Instant end);
}
