package com.amazon.orderservice.infrastructure.persistence;

import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(Order order) {
        return jpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findByStatus(String status) {
        return jpaRepository.findByStatus(status);
    }
}