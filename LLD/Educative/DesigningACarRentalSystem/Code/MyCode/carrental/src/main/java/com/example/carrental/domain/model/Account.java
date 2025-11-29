package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password; // store hashed in production

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String fullName;
    private String email;
    private String phone;

    @Embedded
    private Address address;
}
