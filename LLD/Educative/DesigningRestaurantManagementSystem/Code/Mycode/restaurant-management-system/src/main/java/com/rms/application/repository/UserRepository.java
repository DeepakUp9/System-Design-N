package com.rms.application.repository;

import com.rms.application.model.RmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<RmsUser, Long> {

    // Crucial method used by Spring Security for loading a user by username
    Optional<RmsUser> findByUsername(String username);
}