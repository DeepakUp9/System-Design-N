package com.rms.application.repository;

import com.rms.application.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    // We will use findById in the controller
}