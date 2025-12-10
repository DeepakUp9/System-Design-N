package com.amazon.orderservice.api.controller;

import com.amazon.orderservice.api.dto.OrderRequestDTO;
import com.amazon.orderservice.api.dto.OrderResponseDTO;
import com.amazon.orderservice.api.mapper.OrderMapper;
import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.domain.model.OrderItem;
import com.amazon.orderservice.domain.model.ShippingInfo;
import com.amazon.orderservice.domain.service.OrderService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize; // For production security

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // POST /api/v1/orders - Creates a new order using the LLD Builder Pattern
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')") // Only authenticated users can place orders
    public OrderResponseDTO createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        // 1. Map DTOs to Domain Entities
        List<OrderItem> items = OrderMapper.toOrderItemEntities(requestDTO.items());
        ShippingInfo shippingInfo = OrderMapper.toShippingInfoEntity(requestDTO.shippingInfo());

        // 2. Delegate to the Service (which uses the Builder)
        Order savedOrder = orderService.createOrder(requestDTO.userId(), items, shippingInfo);

        // 3. Map Domain Entity back to DTO for response
        return OrderMapper.toDTO(savedOrder);
    }

    // GET /api/v1/orders/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        // Add check here: if (order.getUserId() != authenticatedUserId) throw new AccessDeniedException()
        return ResponseEntity.ok(OrderMapper.toDTO(order));
    }

    // PUT /api/v1/orders/{id}/pay - Triggers a State Transition (PENDING -> PROCESSING)
    @PutMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponseDTO> processPayment(@PathVariable Long id) {
        // Delegate to the Service (which uses the State Pattern logic)
        orderService.processOrderPayment(id);

        // Fetch the updated order to confirm the new state
        Order updatedOrder = orderService.getOrderById(id);
        return ResponseEntity.ok(OrderMapper.toDTO(updatedOrder));
    }

    // PUT /api/v1/orders/{id}/ship - Triggers a State Transition (PROCESSING -> SHIPPED)
    @PutMapping("/{id}/ship")
    @PreAuthorize("hasRole('ADMIN')") // Only staff/admin can initiate shipping
    public ResponseEntity<OrderResponseDTO> shipOrder(@PathVariable Long id) {
        orderService.shipOrder(id);
        Order updatedOrder = orderService.getOrderById(id);
        return ResponseEntity.ok(OrderMapper.toDTO(updatedOrder));
    }
}