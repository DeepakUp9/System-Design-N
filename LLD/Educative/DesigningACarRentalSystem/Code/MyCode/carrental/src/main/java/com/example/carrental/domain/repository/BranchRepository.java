package com.example.carrental.domain.repository;

import com.example.carrental.domain.model.CarRentalBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BranchRepository extends JpaRepository<CarRentalBranch, UUID> {
}
