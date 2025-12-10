package com.stackclonell.stackclone.repository;

import com.stackclonell.stackclone.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Persistence layer for User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA query method for authentication lookup
    Optional<User> findByUsername(String username);
}