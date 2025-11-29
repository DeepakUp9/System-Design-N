package com.example.carrental.domain.service.impl;

import com.example.carrental.domain.model.Vehicle;
import com.example.carrental.domain.repository.VehicleRepository;
import com.example.carrental.domain.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public List<Vehicle> searchAvailable(com.example.carrental.domain.model.VehicleType type, UUID branchId, Instant start, Instant end) {
        // Use repository query that excludes vehicles with overlapping reservations
        return vehicleRepository.findAvailableByTypeAndBranchAndTimeRange(type, branchId, start, end);
    }
}
