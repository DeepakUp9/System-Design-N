package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "service")
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
}
