package com.example.carrental.service.dto;

import com.example.carrental.domain.model.VehicleReservation;
import com.example.carrental.domain.model.ReservationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class VehicleReservationDto {
    private UUID id;
    private UUID vehicleId;
    private UUID customerId;
    private UUID pickupBranchId;
    private UUID dropoffBranchId;
    private Instant startTime;
    private Instant endTime;
    private ReservationStatus status;
    private BigDecimal estimatedCost;
    private Instant createdAt;
    private Set<UUID> equipmentIds;
    private Set<UUID> serviceIds;

    public static VehicleReservationDto from(VehicleReservation reservation) {
        VehicleReservationDto dto = new VehicleReservationDto();
        dto.setId(reservation.getId());
        dto.setVehicleId(reservation.getVehicle().getId());
        dto.setCustomerId(reservation.getAccount().getId());

        if (reservation.getPickupBranch() != null) {
            dto.setPickupBranchId(reservation.getPickupBranch().getId());
        }
        if (reservation.getDropoffBranch() != null) {
            dto.setDropoffBranchId(reservation.getDropoffBranch().getId());
        }

        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setStatus(reservation.getStatus());
        dto.setEstimatedCost(BigDecimal.valueOf(reservation.getEstimatedCost()));
        dto.setCreatedAt(reservation.getCreatedAt());

        // Convert equipment and service entities to IDs
        if (reservation.getEquipments() != null) {
            dto.setEquipmentIds(reservation.getEquipments().stream()
                    .map(equipment -> equipment.getId())
                    .collect(Collectors.toSet()));
        }

        if (reservation.getServices() != null) {
            dto.setServiceIds(reservation.getServices().stream()
                    .map(service -> service.getId())
                    .collect(Collectors.toSet()));
        }

        return dto;
    }
}