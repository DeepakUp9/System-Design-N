package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "branch")
@NoArgsConstructor
@AllArgsConstructor
public class CarRentalBranch {
    @Id
    private UUID id;

    private String name;

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private Set<ParkingStall> parkingStalls;
}
