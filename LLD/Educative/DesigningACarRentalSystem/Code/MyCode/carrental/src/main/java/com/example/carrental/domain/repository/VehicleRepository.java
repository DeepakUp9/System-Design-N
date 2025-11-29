package com.example.carrental.domain.repository;


import com.example.carrental.domain.model.Vehicle;
import com.example.carrental.domain.model.VehicleType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    // Pessimistic lock when loading a specific vehicle to allocate it
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Vehicle v WHERE v.id = :id")
    Optional<Vehicle> findByIdForUpdate(@Param("id") UUID id);

    // Find candidate vehicles by type & location (no lock) — we'll further filter availability
    @Query("SELECT v FROM Vehicle v WHERE v.vehicleType = :type AND v.branch.id = :branchId AND v.status = 'AVAILABLE'")
    List<Vehicle> findByTypeAndBranchAvailable(@Param("type") VehicleType type,
                                               @Param("branchId") UUID branchId);

    //Alternative: search available vehicles by time range: exclude vehicles having overlapping reservation
    @Query("SELECT v FROM Vehicle v " +
            "WHERE v.branch.id = :branchId " +
            "  AND v.vehicleType = :type " +
            "  AND v.status = 'AVAILABLE' " +
            "  AND v.id NOT IN ( " +
            "    SELECT r.vehicle.id FROM VehicleReservation r " +
            "    WHERE r.status IN ('PENDING', 'CONFIRMED', 'PICKED_UP') " +
            "      AND NOT (r.endTime <= :start OR r.startTime >= :end) " +
            "  )")
    List<Vehicle> findAvailableByTypeAndBranchAndTimeRange(@Param("type") VehicleType type,
                                                           @Param("branchId") UUID branchId,
                                                           @Param("start") Instant start,
                                                           @Param("end") Instant end);
}
