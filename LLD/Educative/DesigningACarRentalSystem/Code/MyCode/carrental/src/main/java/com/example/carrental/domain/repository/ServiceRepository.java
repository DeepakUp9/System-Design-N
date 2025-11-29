package com.example.carrental.domain.repository;

import com.example.carrental.domain.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceRepository extends JpaRepository<Service, UUID> {
}
