package com.rms.application.repository;

import com.rms.application.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

// Provides CRUD and transactional capabilities for the Order entity
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query methods can be added here if needed
}