package com.stockbrokerage.order.api;

import com.stockbrokerage.order.dto.OrderRequest;
import com.stockbrokerage.order.enums.OrderStatus;
import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.service.OrderExecutionService;
import com.stockbrokerage.order.exceptions.OrderProcessingException;
import com.stockbrokerage.queue.OrderCommandQueue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API Endpoint: Exposes the order processing functionality.
 * It strictly handles HTTP concerns and delegates business logic.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderExecutionService orderExecutionService; // The Context component
    private final OrderCommandQueue commandQueue;             // NEW: Command Invoker

    /**
     * Endpoint to submit a new trading order.
     * @param newOrder The order details provided in the request body.
     * @return The processed order with updated status.
     */
    @PostMapping
    public ResponseEntity<String> placeOrder(@Valid @RequestBody OrderRequest request) {

        // 1. Map DTO (Builder output) to the core Domain Entity
        // NOTE: In a clean architecture, a separate Mapper service should handle this.
        Order newOrder = new Order(
                null, null, request.getAccountId(), request.getSymbol(),
                request.getType(), request.getQuantity(), request.getCurrentPrice(),
                request.getLimitPrice(), request.getStopPrice(),
                OrderStatus.NEW, null, null // Initial status is always NEW
        );

        // 2. Production Logic: Submit order to the asynchronous processing queue (COMMAND Pattern)
        // We use the OrderExecutionService for synchronous logic (like validation)
        // and the CommandQueue for high-throughput, asynchronous execution.
        commandQueue.submitOrder(newOrder);

        // Return 202 ACCEPTED, indicating that the request has been accepted for processing.
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Order submitted to processing queue.");
    }
}