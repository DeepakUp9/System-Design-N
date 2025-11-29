package com.example.carrental.domain.repository;

import com.example.carrental.domain.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FineRepository extends JpaRepository<Fine, UUID> {
}
