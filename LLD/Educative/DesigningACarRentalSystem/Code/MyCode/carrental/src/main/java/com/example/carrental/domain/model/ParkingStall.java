package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "parking_stall")
@NoArgsConstructor
@AllArgsConstructor
public class ParkingStall {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private CarRentalBranch branch;

    private String stallCode;

    private String status; // enum possible, keep string for now

    @OneToOne(mappedBy = "parkingStall")
    private Vehicle vehicle;
}
