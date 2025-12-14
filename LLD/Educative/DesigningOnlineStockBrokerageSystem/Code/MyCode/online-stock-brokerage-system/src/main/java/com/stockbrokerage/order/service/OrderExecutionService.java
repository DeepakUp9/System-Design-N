package com.stockbrokerage.order.service;

import com.stockbrokerage.order.enums.OrderStatus;
import com.stockbrokerage.order.enums.OrderType;
import com.stockbrokerage.order.event.OrderProcessedEvent;
import com.stockbrokerage.order.execution.strategy.OrderExecutionStrategy;
import com.stockbrokerage.order.execution.factory.OrderStrategyFactory;
import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.repository.OrderRepository;
import com.stockbrokerage.order.state.OrderState;
import com.stockbrokerage.order.state.OrderStateManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Context Component: The main service that orchestrates order processing.
 * It uses the Factory to get the correct Strategy and delegates the execution.
 */
@Service
@RequiredArgsConstructor // Lombok: Injects final fields (like factory and repository)
public class OrderExecutionService {

    private final OrderStrategyFactory strategyFactory; // Injected Factory
    private final OrderRepository orderRepository;     // Injected Repository (Persistence Layer)
    private final OrderStateManager stateManager;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * The public method for processing any incoming order.
     * @param orderToProcess The new Order object received from the client/API.
     * @return The persisted Order after execution.
     */
    @Transactional // Essential for production level persistence operations
    public Order processOrder(Order orderToProcess) {
        OrderStatus initialStatus = orderToProcess.getStatus(); // NEW: Capture initial status

        // 1. Persistence: Save the initial order state
        Order savedOrder = orderRepository.save(orderToProcess);

        // 2. LLD Core: STATE PATTERN - Transition 1 (Validation)
        OrderState currentState = stateManager.getState(savedOrder.getStatus());
        // The validate() method changes the status within the savedOrder object (e.g., to PENDING_VALIDATION)
        currentState.validate(savedOrder);

        // Check if the order was rejected during validation
        if (savedOrder.getStatus() == OrderStatus.REJECTED) {
            return orderRepository.save(savedOrder);
        }

        // 3. Persistence: Save the state change from Validation
        orderRepository.save(savedOrder);

        // 4. LLD Core: STATE PATTERN - Transition 2 (Processing/Execution)
        // Get the new state object (e.g., PENDING_VALIDATION state)
        currentState = stateManager.getState(savedOrder.getStatus());
        // The process() method will decide the next step
        currentState.process(savedOrder);

        // --- STRATEGY PATTERN LOGIC NOW RUNS WITHIN the State's process() method ---
        // (For production simplicity here, we'll run it immediately after the state change.)

        // 5. LLD Core: STRATEGY PATTERN - Execution
        OrderExecutionStrategy strategy = strategyFactory.getStrategy(savedOrder.getType());
        Order executedOrder = strategy.execute(savedOrder);



        // 6. Persistence: Save the final status
        Order finalOrder = orderRepository.save(executedOrder);

        // 7. LLD Core: OBSERVER PATTERN - Publish the event
        if (finalOrder.getStatus() != initialStatus) { // Only publish if the status actually changed
            eventPublisher.publishEvent(
                    new OrderProcessedEvent(this, finalOrder, initialStatus)
            );
        }

        return  finalOrder;
    }

    // Production-Level Detail: A separate method for cancellation
    @Transactional
    public Order cancelOrder(String orderReferenceId) {
        Order order = orderRepository.findByOrderReferenceId(orderReferenceId)
                .orElseThrow(() -> new OrderProcessingException("Order not found."));

        // Get the current state
        OrderState currentState = stateManager.getState(order.getStatus());

        // Delegate cancellation logic to the State object
        currentState.cancel(order);

        // Save the result (either CANCELLED or an exception was thrown)
        return orderRepository.save(order);
    }
    // ... other order-related methods (cancelOrder, getOrder, etc.)
}