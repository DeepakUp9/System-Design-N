package com.example.carrental.domain.repository;

import com.example.carrental.domain.model.VehicleLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleLogRepository extends JpaRepository<VehicleLog, UUID> {
}
