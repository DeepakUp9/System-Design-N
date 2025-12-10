package com.amazon.orderservice.infrastructure.persistence;

import com.amazon.orderservice.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(String status);
}