package com.example.carrental.domain.repository;

import com.example.carrental.domain.model.ReservationStatus;
import com.example.carrental.domain.model.VehicleReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, UUID> {

    // Check if there are overlapping reservations for a vehicle
    @Query("SELECT COUNT(r) > 0 FROM VehicleReservation r " +
            "WHERE r.vehicle.id = :vehicleId " +
            "AND r.status IN ('PENDING', 'CONFIRMED', 'PICKED_UP') " +
            "AND NOT (r.endTime <= :startTime OR r.startTime >= :endTime)")
    boolean existsOverlapping(@Param("vehicleId") UUID vehicleId,
                              @Param("startTime") Instant startTime,
                              @Param("endTime") Instant endTime);

    // overlapping excluding current reservation
    @Query("""
          SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM VehicleReservation r
          WHERE r.vehicle.id = :vehicleId
            AND r.id <> :excludeId
            AND r.status IN ('PENDING','CONFIRMED','PICKED_UP')
            AND NOT (r.endTime <= :start OR r.startTime >= :end)
        """)
    boolean existsOverlappingExcludingReservation(@Param("vehicleId") UUID vehicleId,
                                                  @Param("start") Instant start,
                                                  @Param("end") Instant end,
                                                  @Param("excludeId") UUID excludeId);

    // check if there are active reservations for a vehicle (exclude cancelled/completed)
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM VehicleReservation r WHERE r.vehicle.id = :vehicleId AND r.status IN ('PENDING','CONFIRMED','PICKED_UP')")
    boolean existsByVehicleIdAndActiveStatuses(@Param("vehicleId") UUID vehicleId);

    // Add this method for the scheduler
    @Query("SELECT r FROM VehicleReservation r WHERE r.endTime < :now AND r.status IN :statuses")
    List<VehicleReservation> findByEndTimeBeforeAndStatusIn(@Param("now") Instant now,
                                                            @Param("statuses") List<ReservationStatus> statuses);

}