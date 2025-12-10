package com.stackclonell.stackclone.repository;

import com.stackclonell.stackclone.core.model.Badge;
import com.stackclonell.stackclone.core.model.BadgeRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    // Check if a user already has a specific badge (e.g., Bronze Enthusiast)
    Optional<Badge> findByUserUsernameAndNameAndRank(String username, String name, BadgeRank rank);
}