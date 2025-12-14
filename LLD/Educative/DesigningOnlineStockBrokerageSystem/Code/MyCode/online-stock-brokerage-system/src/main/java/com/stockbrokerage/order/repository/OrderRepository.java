package com.stockbrokerage.order.repository;

import com.stockbrokerage.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Persistence Layer: Provides CRUD operations for the Order entity.
 * Spring Data JPA handles the implementation details automatically.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query methods can be added here if needed,
    // e.g., List<Order> findByAccountId(Long accountId);
}