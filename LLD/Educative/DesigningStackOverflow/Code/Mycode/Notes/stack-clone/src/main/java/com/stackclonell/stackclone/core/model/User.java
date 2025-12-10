package com.stackclonell.stackclone.core.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "platform_user") // Avoid conflict with reserved 'user'
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private Long reputation = 1L; // Stack Overflow starts users at 1
    private LocalDateTime memberSince = LocalDateTime.now();

    // Security consideration: Password hash stored here (will be managed by Spring Security)
    private String passwordHash;
}