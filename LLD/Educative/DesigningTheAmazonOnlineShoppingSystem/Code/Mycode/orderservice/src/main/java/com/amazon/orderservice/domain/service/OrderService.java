package com.amazon.orderservice.domain.service;

import com.amazon.orderservice.domain.builder.ConcreteOrderBuilder;
import com.amazon.orderservice.domain.model.Order;
import com.amazon.orderservice.domain.model.OrderItem;
import com.amazon.orderservice.domain.model.ShippingInfo;
import com.amazon.orderservice.domain.repository.OrderRepository;
import com.amazon.orderservice.domain.state.OrderState;
import com.amazon.orderservice.domain.exception.ResourceNotFoundException;

import com.amazon.orderservice.infrastructure.messaging.OrderEventPublisher;
import org.springframework.context.ApplicationContext; // Used for State Factory
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationContext applicationContext; // DI for State Pattern Factory
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, ApplicationContext applicationContext, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.applicationContext = applicationContext;
        this.eventPublisher = eventPublisher;
    }

    /**
     * LLD Builder Pattern Usage: Handles complex Order creation.
     */
    @Transactional
    public Order createOrder(Long userId, List<OrderItem> items, ShippingInfo shippingInfo) {
        // 1. ConcreteOrderBuilder is used to assemble the Order
        ConcreteOrderBuilder builder = Order.builder();

        Order order = builder
                .setUserId(userId)
                .setShippingInfo(shippingInfo)
                //.addLineItems(items) // Assuming this method is added to ConcreteOrderBuilder
                .calculateTotal()
                .build();

        // 2. Persistence
        Order savedOrder = orderRepository.save(order);

        // 3. State Initialization (Setting the initial PENDING state)
        initializeOrderState(savedOrder);

        // Business Logic: Initiate inventory reservation check (Asynchronous step via Message Broker in HLD)

        // --- OBSERVER PATTERN TRIGGER ---
        // This is the moment the Subject (Order) notifies its Observers (Catalog)
        eventPublisher.publishOrderPlacedEvent(savedOrder);

        return savedOrder;
    }

    /**
     * LLD State Pattern Usage: Delegates actions based on current status.
     */
    @Transactional
    public void processOrderPayment(Long orderId) {
        Order order = getOrderById(orderId);

        // The service initializes the state and delegates the action to it.
        initializeOrderState(order);
        order.processPayment();

        // Persistence: Save the order after its state has potentially changed.
        orderRepository.save(order);
    }

    @Transactional
    public void shipOrder(Long orderId) {
        Order order = getOrderById(orderId);
        initializeOrderState(order);
        order.shipOrder();
        orderRepository.save(order);
    }

    // --- Helper Methods ---

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        Order order = orderOptional.get();
        initializeOrderState(order); // Important: Must set the state after loading
        return order;
    }

    /**
     * State Factory/Initialization: Uses Spring's ApplicationContext to fetch the correct
     * singleton State implementation based on the status string.
     */
    private void initializeOrderState(Order order) {
        // Get the state bean using the status name (e.g., "PENDING") as the bean name
        String statusName = order.getStatus();

        try {
            OrderState stateBean = (OrderState) applicationContext.getBean(statusName);
            order.setCurrentState(stateBean);
        } catch (Exception e) {
            // Production Standard: Handle case where a persisted status doesn't match an available state class
            System.err.println("Error initializing state '" + statusName + "': " + e.getMessage());
            throw new IllegalStateException("Invalid order status: " + statusName);
        }
    }
}