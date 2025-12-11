package com.rms.application.controller;

import com.rms.application.dto.CreateOrderRequest;
import com.rms.application.dto.OrderResponse;
import com.rms.application.mapper.OrderMapper;
import com.rms.application.model.Branch;
import com.rms.application.model.Order;
import com.rms.application.repository.BranchRepository;
import com.rms.application.service.OrderService;
import com.rms.core.model.OrderStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final BranchRepository branchRepository; // Need this temporarily for setting up Branch FK

    public OrderController(OrderService orderService,
                           OrderMapper orderMapper,
                           BranchRepository branchRepository) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.branchRepository = branchRepository;
    }

    /**
     * Endpoint to create a new order.
     * Accessible by WAITER or ONLINE_HANDLER roles (from Step 3 SecurityConfig).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        // Step 1: Prepare the JPA entity from DTO
        // NOTE: In a real system, the client provides line items, and the service calculates totalAmount.

        Optional<Branch> branch = branchRepository.findById(request.getBranchId());
        if (branch.isEmpty()) {
            // Handle error: Branch not found
            throw new IllegalArgumentException("Branch not found with ID: " + request.getBranchId());
        }

        Order newOrder = new Order();
        newOrder.setBranch(branch.get());
        newOrder.setChannelType(request.getChannelType());
        newOrder.setTotalAmount(request.getTotalAmount());

        // Step 2: Persist the initial NEW order
        Order createdOrder = orderService.createOrder(newOrder);

        // Step 3: Immediately process the order (triggers State/Strategy logic)
        // This is done to ensure the NEW order moves immediately to PROCESSING and fulfillment begins.
        Order processedOrder = orderService.processNewOrder(createdOrder.getId(), createdOrder.getChannelType());

        return orderMapper.toResponse(processedOrder);
    }

    /**
     * Endpoint to simulate a state transition from the kitchen/staff.
     * Accessible by KITCHEN or WAITER roles.
     * Triggers the OrderState.fulfillOrder() method.
     */
    @PutMapping("/{orderId}/fulfill")
    @PreAuthorize("hasAnyRole('KITCHEN', 'WAITER')")
    public OrderResponse fulfillOrder(@PathVariable Long orderId) {
        // Calls the service layer which initiates the State Pattern logic
        Order fulfilledOrder = orderService.fulfillOrder(orderId);

        return orderMapper.toResponse(fulfilledOrder);
    }

    // We need to implement a repository for Branch to look it up during order creation.
    // For now, let's add the interface definition.
}