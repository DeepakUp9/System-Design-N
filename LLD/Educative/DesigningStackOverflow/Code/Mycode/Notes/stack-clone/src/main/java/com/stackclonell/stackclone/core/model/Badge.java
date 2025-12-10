package com.stackclonell.stackclone.core.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who earned the badge
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name; // e.g., "Enthusiast", "Silver Questioner"

    @Enumerated(EnumType.STRING)
    private BadgeRank rank; // e.g., BRONZE, SILVER, GOLD

    private LocalDateTime awardedDate = LocalDateTime.now();
}