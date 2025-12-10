package com.amazon.orderservice.domain.repository;

import com.amazon.orderservice.domain.model.Order;
import java.util.Optional;
import java.util.List;

// LLD Repository Interface - Decoupled from Spring JPA
public interface OrderRepository {

    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(String status);
    // Note: No delete method, as orders are usually soft-deleted or archived, not truly deleted.
}