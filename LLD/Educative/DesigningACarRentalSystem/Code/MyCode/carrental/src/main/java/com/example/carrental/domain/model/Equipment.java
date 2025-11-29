package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "equipment")
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @Id
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
}
