package com.example.carrental.domain.repository;

import com.example.carrental.domain.model.ParkingStall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParkingStallRepository extends JpaRepository<ParkingStall, UUID> {
}
